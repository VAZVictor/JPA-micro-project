package at.htl.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static at.htl.api.TestData.createArtist;
import static at.htl.api.TestData.createDevice;
import static at.htl.api.TestData.createTrack;
import static at.htl.api.TestData.createUser;
import static at.htl.api.TestData.recordSession;
import static at.htl.api.TestData.unique;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class UserResourceTest {

    @Test
    void createUserReturnsCreatedUser() {
        var username = unique("neu");
        given()
                .contentType("application/json")
                .body(Map.of("username", username, "email", "neu@soundvault.at"))
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .body("username", is(username));
    }

    @Test
    void createUserRejectsDuplicateAndInvalid() {
        given().contentType("application/json").body(Map.of("username", "elena"))
                .when().post("/api/users").then().statusCode(400);
        given().contentType("application/json").body(Map.of("username", unique("x"), "email", "keine-mail"))
                .when().post("/api/users").then().statusCode(400);
    }

    @Test
    void getAllUsersReturnsSeeded() {
        given().when().get("/api/users").then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3))
                .body("username", hasItems("elena", "victor", "annika"));
    }

    @Test
    void devicesOfSeededUser() {
        given().when().get("/api/users/1/devices").then()
                .statusCode(200)
                .body("size()", is(2))
                .body("type", hasItems("PHONE", "WATCH"));
        given().when().get("/api/users/999999/devices").then().statusCode(404);
    }

    @Test
    void addDeviceValidatesType() {
        var userId = createUser();
        given().contentType("application/json").body(Map.of("name", "Box", "type", "SPEAKER"))
                .when().post("/api/users/{id}/devices", userId).then()
                .statusCode(200)
                .body("type", is("SPEAKER"))
                .body("userId", is(userId));
        given().contentType("application/json").body(Map.of("name", "Toaster", "type", "TOASTER"))
                .when().post("/api/users/{id}/devices", userId).then().statusCode(400);
    }

    @Test
    void favoritesOfSeededUser() {
        given().when().get("/api/users/1/favorites").then()
                .statusCode(200)
                .body("title", hasItems("Bungalow", "Warum JPA?"));
    }

    @Test
    void addAndRemoveFavorite() {
        var userId = createUser();

        given().contentType("application/json").body(Map.of("audioItemId", 3))
                .when().post("/api/users/{id}/favorites", userId).then()
                .statusCode(200)
                .body("title", is("Bologna"))
                .body("artistName", is("Wanda"));

        given().contentType("application/json").body(Map.of("audioItemId", 3))
                .when().post("/api/users/{id}/favorites", userId).then().statusCode(400);

        given().when().delete("/api/users/{id}/favorites/3", userId).then().statusCode(204);
        given().when().get("/api/users/{id}/favorites", userId).then()
                .statusCode(200)
                .body("audioItemId", not(hasItem(3)));
        given().when().delete("/api/users/{id}/favorites/3", userId).then().statusCode(404);
    }

    @Test
    void favoriteForUnknownItemReturns404() {
        given().contentType("application/json").body(Map.of("audioItemId", 999999))
                .when().post("/api/users/1/favorites").then().statusCode(404);
    }

    @Test
    void recordedSessionAppearsInHistory() {
        var trackId = createTrack(createArtist(), 200);
        var userId = createUser();
        var deviceId = createDevice(userId);
        recordSession(userId, deviceId, trackId, 100);

        given().when().get("/api/users/{id}/listening-sessions", userId).then()
                .statusCode(200)
                .body("size()", is(1))
                .body("audioItemId[0]", is(trackId))
                .body("listenedSeconds[0]", is(100));
    }

    @Test
    void sessionWithForeignDeviceIsRejected() {
        var userId = createUser();
        given().contentType("application/json")
                .body(Map.of("userId", userId, "deviceId", 1, "audioItemId", 2,
                        "startedAt", "2026-11-20T10:00:00", "listenedSeconds", 10))
                .when().post("/api/listening-sessions").then().statusCode(400);
    }

    @Test
    void sessionLongerThanItemIsRejected() {
        var userId = createUser();
        var deviceId = createDevice(userId);
        given().contentType("application/json")
                .body(Map.of("userId", userId, "deviceId", deviceId, "audioItemId", 2,
                        "startedAt", "2026-11-20T10:00:00", "listenedSeconds", 10000))
                .when().post("/api/listening-sessions").then().statusCode(400);
    }

    @Test
    void sessionInTheFutureIsRejected() {
        var userId = createUser();
        var deviceId = createDevice(userId);
        given().contentType("application/json")
                .body(Map.of("userId", userId, "deviceId", deviceId, "audioItemId", 2,
                        "startedAt", "2999-01-01T10:00:00", "listenedSeconds", 10))
                .when().post("/api/listening-sessions").then().statusCode(400);
    }

    @Test
    void sessionForUnknownUserReturns404() {
        given().contentType("application/json")
                .body(Map.of("userId", 999999, "deviceId", 1, "audioItemId", 2,
                        "startedAt", "2026-11-20T10:00:00", "listenedSeconds", 10))
                .when().post("/api/listening-sessions").then().statusCode(404);
    }
}
