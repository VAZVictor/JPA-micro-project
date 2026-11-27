package at.htl.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static at.htl.api.TestData.createArtist;
import static at.htl.api.TestData.createDevice;
import static at.htl.api.TestData.createTrack;
import static at.htl.api.TestData.createUser;
import static at.htl.api.TestData.recordSession;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
class AudioItemResourceTest {

    @Test
    void getAllAudioItemsIsPolymorphic() {
        given()
                .when()
                .get("/api/audio-items")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(9))
                .body("find { it.title == 'Bungalow' }.type", is("TRACK"))
                .body("find { it.title == 'Warum JPA?' }.type", is("PODCAST_EPISODE"))
                .body("find { it.title == 'Bungalow' }.genres", hasItems("Austropop", "Pop"));
    }

    @Test
    void getAudioItemById() {
        given().when().get("/api/audio-items/8").then().statusCode(200)
                .body("type", is("PODCAST_EPISODE"))
                .body("artistName", is("Lena Hofer"));
        given().when().get("/api/audio-items/999999").then().statusCode(404);
    }

    @Test
    void unusedContainsItemsWithoutSessionInRange() {
        given()
                .when()
                .get("/api/audio-items/unused?from=2026-11-01&to=2026-11-30")
                .then()
                .statusCode(200)
                .body("title", hasItems("One More Time", "Joins verstehen", "Luzia"))
                .body("title", not(hasItem("Bungalow")))
                .body("find { it.title == 'Luzia' }.playlistCount", is(1))
                .body("find { it.title == 'One More Time' }.playlistCount", is(0));
    }

    @Test
    void unusedDependsOnTimeRange() {
        given()
                .when()
                .get("/api/audio-items/unused?from=2026-11-02&to=2026-11-02")
                .then()
                .statusCode(200)
                .body("title", not(hasItem("Bungalow")))
                .body("title", hasItem("Maschin"));
    }

    @Test
    void unusedRejectsInvalidRange() {
        given().when().get("/api/audio-items/unused?from=2026-11-30&to=2026-11-01").then().statusCode(400);
        given().when().get("/api/audio-items/unused?to=2026-11-01").then().statusCode(400);
    }

    @Test
    void newTrackIsUnusedUntilPlayed() {
        var trackId = createTrack(createArtist(), 200);
        given().when().get("/api/audio-items/unused?from=2026-11-01&to=2026-11-30").then()
                .statusCode(200)
                .body("id", hasItem(trackId));

        var userId = createUser();
        recordSession(userId, createDevice(userId), trackId, 100);

        given().when().get("/api/audio-items/unused?from=2026-11-01&to=2026-11-30").then()
                .statusCode(200)
                .body("id", not(hasItem(trackId)));
    }

    @Test
    void createTrackWithAlbumAndGenres() {
        given()
                .contentType("application/json")
                .body(Map.of("title", "Plansch", "durationSeconds", 210, "artistId", 1,
                        "albumId", 1, "trackNumber", 4, "genreIds", List.of(1, 4)))
                .when()
                .post("/api/tracks")
                .then()
                .statusCode(200)
                .body("albumTitle", is("Schick Schock"))
                .body("trackNumber", is(4))
                .body("genres", hasItems("Austropop", "Pop"));
    }

    @Test
    void createTrackRejectsAlbumOfOtherArtist() {
        given()
                .contentType("application/json")
                .body(Map.of("title", "Falsch", "durationSeconds", 210, "artistId", 2, "albumId", 1))
                .when()
                .post("/api/tracks")
                .then()
                .statusCode(400);
    }

    @Test
    void createTrackRejectsUnknownReferences() {
        given().contentType("application/json")
                .body(Map.of("title", "X", "durationSeconds", 100, "artistId", 999999))
                .when().post("/api/tracks").then().statusCode(404);
        given().contentType("application/json")
                .body(Map.of("title", "X", "durationSeconds", 100, "artistId", 1, "genreIds", List.of(999999)))
                .when().post("/api/tracks").then().statusCode(404);
    }

    @Test
    void createTrackRejectsInvalidRequest() {
        given().contentType("application/json")
                .body(Map.of("title", "X", "durationSeconds", 0, "artistId", 1))
                .when().post("/api/tracks").then().statusCode(400);
        given().contentType("application/json")
                .body(Map.of("durationSeconds", 100, "artistId", 1))
                .when().post("/api/tracks").then().statusCode(400);
    }

    @Test
    void updateTrackChangesManagedEntity() {
        var trackId = createTrack(createArtist(), 200);

        given()
                .contentType("application/json")
                .body(Map.of("title", "Neuer Titel", "durationSeconds", 250, "trackNumber", 2,
                        "genreIds", List.of(3)))
                .when()
                .put("/api/tracks/{id}", trackId)
                .then()
                .statusCode(200)
                .body("title", is("Neuer Titel"));

        given()
                .when()
                .get("/api/tracks/{id}", trackId)
                .then()
                .statusCode(200)
                .body("title", is("Neuer Titel"))
                .body("durationSeconds", is(250))
                .body("trackNumber", is(2))
                .body("genres", is(List.of("Electronic")));
    }

    @Test
    void updateUnknownTrackReturns404() {
        given()
                .contentType("application/json")
                .body(Map.of("title", "X", "durationSeconds", 100))
                .when()
                .put("/api/tracks/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteUnusedTrack() {
        var trackId = createTrack(createArtist(), 200);
        given().when().delete("/api/tracks/{id}", trackId).then().statusCode(204);
        given().when().get("/api/tracks/{id}", trackId).then().statusCode(404);
    }

    @Test
    void deleteReferencedTrackReturns409() {
        given().when().delete("/api/tracks/1").then().statusCode(409);
        given().when().get("/api/tracks/1").then().statusCode(200);
    }

    @Test
    void singleHasNoAlbum() {
        given()
                .when()
                .get("/api/tracks/7")
                .then()
                .statusCode(200)
                .body("title", is("One More Time"))
                .body("albumId", nullValue());
    }

    @Test
    void podcastEpisodeCanBeCreatedAndListed() {
        given()
                .contentType("application/json")
                .body(Map.of("title", "Indizes", "durationSeconds", 1500, "hostId", 4,
                        "showName", "Datenbank-Talk", "episodeNumber", 3, "publishedAt", "2026-11-01"))
                .when()
                .post("/api/podcast-episodes")
                .then()
                .statusCode(200)
                .body("hostName", is("Lena Hofer"))
                .body("episodeNumber", is(3));

        given()
                .when()
                .get("/api/podcast-episodes")
                .then()
                .statusCode(200)
                .body("title", hasItems("Warum JPA?", "Joins verstehen", "Indizes"));
    }
}
