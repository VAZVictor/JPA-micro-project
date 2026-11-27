package at.htl.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static at.htl.api.TestData.createArtist;
import static at.htl.api.TestData.createDevice;
import static at.htl.api.TestData.createTrack;
import static at.htl.api.TestData.createUser;
import static at.htl.api.TestData.recordSession;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class ArtistResourceTest {

    @Test
    void createArtistReturnsCreatedArtist() {
        given()
                .contentType("application/json")
                .body(Map.of("name", "Neue Band", "country", "Austria"))
                .when()
                .post("/api/artists")
                .then()
                .statusCode(200)
                .body("name", is("Neue Band"))
                .body("country", is("Austria"));
    }

    @Test
    void createArtistRejectsBlankName() {
        given()
                .contentType("application/json")
                .body(Map.of("name", ""))
                .when()
                .post("/api/artists")
                .then()
                .statusCode(400);
    }

    @Test
    void getAllArtistsReturnsSeeded() {
        given()
                .when()
                .get("/api/artists")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(5))
                .body("name", hasItem("Bilderbuch"));
    }

    @Test
    void topArtistsAreRankedByListeningTime() {
        given()
                .when()
                .get("/api/artists/top?size=3")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("name[0]", is("Lena Hofer"))
                .body("totalSeconds[0]", is(1800))
                .body("name[1]", is("Bilderbuch"))
                .body("totalSeconds[1]", is(663))
                .body("sessionCount[1]", is(3));
    }

    @Test
    void topArtistsHavingIncludesArtistExactlyOnThreshold() {
        given()
                .when()
                .get("/api/artists/top?minSeconds=489")
                .then()
                .statusCode(200)
                .body("name", hasItem("Daft Punk"))
                .body("name", not(hasItem("Wanda")));

        given()
                .when()
                .get("/api/artists/top?minSeconds=490")
                .then()
                .statusCode(200)
                .body("name", not(hasItem("Daft Punk")))
                .body("name", hasItem("Bilderbuch"));
    }

    @Test
    void topArtistsSupportsPaging() {
        given()
                .when()
                .get("/api/artists/top?page=1&size=1")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("name[0]", is("Bilderbuch"));
    }

    @Test
    void topArtistsAboveEveryoneIsEmpty() {
        given()
                .when()
                .get("/api/artists/top?minSeconds=1000000")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void topArtistsRejectsInvalidPaging() {
        given().when().get("/api/artists/top?size=0").then().statusCode(400);
        given().when().get("/api/artists/top?page=-1").then().statusCode(400);
        given().when().get("/api/artists/top?minSeconds=-5").then().statusCode(400);
    }

    @Test
    void newSessionCountsForArtist() {
        var artistId = createArtist();
        var trackId = createTrack(artistId, 100);
        var userId = createUser();
        var deviceId = createDevice(userId);
        recordSession(userId, deviceId, trackId, 90);
        recordSession(userId, deviceId, trackId, 60);

        given()
                .when()
                .get("/api/artists/top?minSeconds=150&size=100")
                .then()
                .statusCode(200)
                .body("find { it.artistId == %d }.totalSeconds".formatted(artistId), is(150))
                .body("find { it.artistId == %d }.sessionCount".formatted(artistId), is(2));
    }
}
