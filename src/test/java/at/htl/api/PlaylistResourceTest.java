package at.htl.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.htl.api.TestData.createPlaylist;
import static at.htl.api.TestData.createUser;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

@QuarkusTest
class PlaylistResourceTest {

    @Test
    void getPlaylistReturnsEntriesInOrder() {
        given()
                .when()
                .get("/api/playlists/1")
                .then()
                .statusCode(200)
                .body("name", is("Morgenroutine"))
                .body("username", is("elena"))
                .body("entries.position", is(List.of(1, 2, 3)))
                .body("entries.title", is(List.of("Bungalow", "Bologna", "Luzia")));
    }

    @Test
    void getUnknownPlaylistReturns404() {
        given().when().get("/api/playlists/999999").then().statusCode(404);
        given().when().get("/api/playlists/999999/overview").then().statusCode(404);
    }

    @Test
    void overviewComputesCountAndDuration() {
        given()
                .when()
                .get("/api/playlists/1/overview")
                .then()
                .statusCode(200)
                .body("entryCount", is(3))
                .body("totalDurationSeconds", is(656))
                .body("lastAddedAt", startsWith("2026-10-10T07:03"));
    }

    @Test
    void overviewOfEmptyPlaylistHasZeroValues() {
        given()
                .when()
                .get("/api/playlists/3/overview")
                .then()
                .statusCode(200)
                .body("entryCount", is(0))
                .body("totalDurationSeconds", is(0))
                .body("lastAddedAt", nullValue());
    }

    @Test
    void overviewListCanBeFilteredByUser() {
        given()
                .when()
                .get("/api/playlists?userId=2")
                .then()
                .statusCode(200)
                .body("username", everyItem(is("victor")))
                .body("name", hasItem("Fokus"))
                .body("find { it.name == 'Fokus' }.entryCount", is(3));

        given().when().get("/api/playlists?userId=999999").then().statusCode(404);
    }

    @Test
    void createPlaylistCreatesMasterAndDetailsInOneStep() {
        var userId = createUser();

        var id = given()
                .contentType("application/json")
                .body(Map.of("userId", userId, "name", "Mix", "description", "Test",
                        "audioItemIds", List.of(5, 1, 5)))
                .when()
                .post("/api/playlists")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("entries.position", is(List.of(1, 2, 3)))
                .body("entries.audioItemId", is(List.of(5, 1, 5)))
                .extract()
                .<Integer>path("id");

        given()
                .when()
                .get("/api/playlists/{id}/overview", id)
                .then()
                .statusCode(200)
                .body("entryCount", is(3))
                .body("totalDurationSeconds", is(369 + 227 + 369));
    }

    @Test
    void createPlaylistWithUnknownItemCreatesNothing() {
        var userId = createUser();

        given()
                .contentType("application/json")
                .body(Map.of("userId", userId, "name", "Kaputt", "audioItemIds", List.of(1, 999999)))
                .when()
                .post("/api/playlists")
                .then()
                .statusCode(404);

        given()
                .when()
                .get("/api/playlists?userId={id}", userId)
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void createPlaylistRejectsInvalidRequest() {
        var body = new HashMap<String, Object>();
        body.put("userId", 1);
        body.put("name", "");
        given().contentType("application/json").body(body)
                .when().post("/api/playlists").then().statusCode(400);

        given().contentType("application/json").body(Map.of("name", "Ohne User"))
                .when().post("/api/playlists").then().statusCode(400);

        given().contentType("application/json").body(Map.of("userId", 999999, "name", "X"))
                .when().post("/api/playlists").then().statusCode(404);
    }

    @Test
    void updatePlaylistRenamesIt() {
        var id = createPlaylist(createUser(), List.of(1));

        given()
                .contentType("application/json")
                .body(Map.of("name", "Umbenannt", "description", "Neu"))
                .when()
                .put("/api/playlists/{id}", id)
                .then()
                .statusCode(200)
                .body("name", is("Umbenannt"));

        given().when().get("/api/playlists/{id}", id).then()
                .statusCode(200)
                .body("name", is("Umbenannt"))
                .body("description", is("Neu"));
    }

    @Test
    void reorderMovesEntries() {
        var id = createPlaylist(createUser(), List.of(1, 2, 3));

        given()
                .contentType("application/json")
                .body(Map.of("positions", List.of(3, 1, 2)))
                .when()
                .put("/api/playlists/{id}/order", id)
                .then()
                .statusCode(200)
                .body("entries.audioItemId", is(List.of(3, 1, 2)))
                .body("entries.position", is(List.of(1, 2, 3)));

        given().when().get("/api/playlists/{id}", id).then()
                .statusCode(200)
                .body("entries.audioItemId", is(List.of(3, 1, 2)));
    }

    @Test
    void reorderRejectsIncompletePositions() {
        var id = createPlaylist(createUser(), List.of(1, 2, 3));

        given().contentType("application/json").body(Map.of("positions", List.of(1, 2)))
                .when().put("/api/playlists/{id}/order", id).then().statusCode(400);
        given().contentType("application/json").body(Map.of("positions", List.of(1, 1, 2)))
                .when().put("/api/playlists/{id}/order", id).then().statusCode(400);
        given().contentType("application/json").body(Map.of("positions", List.of(1, 2, 4)))
                .when().put("/api/playlists/{id}/order", id).then().statusCode(400);
        given().contentType("application/json").body(Map.of("positions", List.of()))
                .when().put("/api/playlists/{id}/order", id).then().statusCode(400);
    }

    @Test
    void addEntryAppendsAtTheEnd() {
        var id = createPlaylist(createUser(), List.of(1));

        given()
                .contentType("application/json")
                .body(Map.of("audioItemId", 8))
                .when()
                .post("/api/playlists/{id}/entries", id)
                .then()
                .statusCode(200)
                .body("entries.position", is(List.of(1, 2)))
                .body("entries.audioItemId", is(List.of(1, 8)));
    }

    @Test
    void removeEntryClosesTheGap() {
        var id = createPlaylist(createUser(), List.of(1, 2, 3));

        given()
                .when()
                .delete("/api/playlists/{id}/entries/2", id)
                .then()
                .statusCode(200)
                .body("entries.position", is(List.of(1, 2)))
                .body("entries.audioItemId", is(List.of(1, 3)));

        given().when().delete("/api/playlists/{id}/entries/9", id).then().statusCode(404);
    }

    @Test
    void deletePlaylistRemovesEntriesToo() {
        var id = createPlaylist(createUser(), List.of(1, 2));

        given().when().delete("/api/playlists/{id}", id).then().statusCode(204);
        given().when().get("/api/playlists/{id}", id).then().statusCode(404);
        given().when().get("/api/playlists").then().statusCode(200).body("playlistId", not(hasItem(id)));
    }
}
