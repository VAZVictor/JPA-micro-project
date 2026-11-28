package at.htl.workloads.artist;

import at.htl.error.InvalidRequestException;
import at.htl.model.artist.ArtistListeningDTO;
import at.htl.model.listeningSession.CreateListeningSessionRequest;
import at.htl.workloads.listeningSession.ListeningSessionService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class TopArtistsQueryTest {

    private static final long ABOVE_TEST_DATA = 200L;

    @Inject
    ArtistService artistService;

    @Inject
    ListeningSessionService sessionService;

    @Test
    void defaultsToFirstPageWithoutThreshold() {
        var top = names(artistService.getTopArtists(null, null, null));

        assertTrue(top.size() <= 10);
        assertEquals("Lena Hofer", top.get(0));
    }

    @Test
    void ranksArtistsByAggregatedListeningTime() {
        var top = artistService.getTopArtists(ABOVE_TEST_DATA, null, null);

        assertEquals(List.of("Lena Hofer", "Bilderbuch", "Daft Punk", "Wanda"), names(top));
        assertEquals(1800L, top.get(0).totalSeconds());
        assertEquals(663L, top.get(1).totalSeconds());
        assertEquals(3L, top.get(1).sessionCount());
    }

    @Test
    void havingKeepsArtistExactlyOnThreshold() {
        assertTrue(names(artistService.getTopArtists(489L, 0, 10)).contains("Daft Punk"));
    }

    @Test
    void havingDropsArtistOneSecondBelowThreshold() {
        var names = names(artistService.getTopArtists(490L, 0, 10));

        assertFalse(names.contains("Daft Punk"));
        assertEquals(List.of("Lena Hofer", "Bilderbuch"), names);
    }

    @Test
    void noHitsAboveHighestTotal() {
        assertTrue(artistService.getTopArtists(1801L, 0, 10).isEmpty());
    }

    @Test
    void artistWithoutSessionsNeverAppears() {
        assertFalse(names(artistService.getTopArtists(0L, 0, 100)).contains("Leere Band"));
    }

    @Test
    void pagesThroughRanking() {
        assertEquals(List.of("Lena Hofer", "Bilderbuch"),
                names(artistService.getTopArtists(ABOVE_TEST_DATA, 0, 2)));
        assertEquals(List.of("Daft Punk", "Wanda"),
                names(artistService.getTopArtists(ABOVE_TEST_DATA, 1, 2)));
        assertTrue(artistService.getTopArtists(ABOVE_TEST_DATA, 2, 2).isEmpty());
    }

    @Test
    @TestTransaction
    void newSessionChangesRanking() {
        sessionService.recordSession(new CreateListeningSessionRequest(
                2L, 3L, 4L, LocalDateTime.of(2026, 11, 20, 10, 0), 224));
        sessionService.recordSession(new CreateListeningSessionRequest(
                2L, 3L, 4L, LocalDateTime.of(2026, 11, 21, 10, 0), 224));

        var top = artistService.getTopArtists(ABOVE_TEST_DATA, 0, 10);

        assertEquals(List.of("Lena Hofer", "Bilderbuch", "Wanda", "Daft Punk"), names(top));
        assertEquals(653L, top.get(2).totalSeconds());
    }

    @Test
    void rejectsInvalidParameters() {
        assertThrows(InvalidRequestException.class, () -> artistService.getTopArtists(-1L, 0, 10));
        assertThrows(InvalidRequestException.class, () -> artistService.getTopArtists(0L, -1, 10));
        assertThrows(InvalidRequestException.class, () -> artistService.getTopArtists(0L, 0, 0));
        assertThrows(InvalidRequestException.class, () -> artistService.getTopArtists(0L, 0, 101));
    }

    private static List<String> names(List<ArtistListeningDTO> artists) {
        return artists.stream().map(ArtistListeningDTO::name).toList();
    }
}
