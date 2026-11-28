package at.htl.workloads;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class SoundVaultResourceTest {

    @Test
    void shouldGetUnusedAudioItemsEndpoint() {
        given()
                .queryParam("from", "2026-11-01")
                .queryParam("to", "2026-11-30")
                .when()
                .get("/api/audio-items/unused")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void shouldReturnNotFoundForNonExistentPlaylistOverview() {
        given()
                .when()
                .get("/api/playlists/99999/overview")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldCreateNewArtistSuccessfully() {
        String requestBody = """
            {
              "name": "Thees Uhlmann",
              "country": "Germany"
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/artists")
                .then()
                .statusCode(200)
                .body("name", is("Thees Uhlmann"));
    }

    @Test
    void shouldReturnBadRequestWhenArtistNameIsMissing() {
        String invalidBody = """
            {
              "country": "Germany"
            }
            """;

        given()
                .contentType("application/json")
                .body(invalidBody)
                .when()
                .post("/api/artists")
                .then()
                .statusCode(400);
    }
    @Test
    void shouldReturnBadRequestWhenCreatingArtistWithEmptyName() {
        String invalidBody = """
        {
          "name": "",
          "country": "Austria"
        }
        """;

        given()
                .contentType("application/json")
                .body(invalidBody)
                .when()
                .post("/api/artists")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnBadRequestWhenUnusedEndpointMissingParams() {
        given()
                .when()
                .get("/api/audio-items/unused")
                .then()
                .statusCode(400); // Da from/to als Pflichtparameter definiert sind
    }
}