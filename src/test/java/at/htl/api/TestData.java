package at.htl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;

final class TestData {

    private static final AtomicInteger SEQ = new AtomicInteger();

    private TestData() {
    }

    static String unique(String prefix) {
        return prefix + SEQ.incrementAndGet();
    }

    static int createArtist() {
        return given()
                .contentType("application/json")
                .body(Map.of("name", unique("Artist "), "country", "Austria"))
                .when()
                .post("/api/artists")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    static int createTrack(int artistId, int durationSeconds) {
        var body = new HashMap<String, Object>();
        body.put("title", unique("Track "));
        body.put("durationSeconds", durationSeconds);
        body.put("artistId", artistId);
        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/api/tracks")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    static int createUser() {
        return given()
                .contentType("application/json")
                .body(Map.of("username", unique("user"), "email", "test@soundvault.at"))
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    static int createDevice(int userId) {
        return given()
                .contentType("application/json")
                .body(Map.of("name", unique("Device "), "type", "PHONE"))
                .when()
                .post("/api/users/{id}/devices", userId)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    static void recordSession(int userId, int deviceId, int audioItemId, int seconds) {
        given()
                .contentType("application/json")
                .body(Map.of("userId", userId, "deviceId", deviceId, "audioItemId", audioItemId,
                        "startedAt", "2026-11-20T10:00:00", "listenedSeconds", seconds))
                .when()
                .post("/api/listening-sessions")
                .then()
                .statusCode(200);
    }

    static int createPlaylist(int userId, List<Integer> audioItemIds) {
        return given()
                .contentType("application/json")
                .body(Map.of("userId", userId, "name", unique("Playlist "), "audioItemIds", audioItemIds))
                .when()
                .post("/api/playlists")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }
}
