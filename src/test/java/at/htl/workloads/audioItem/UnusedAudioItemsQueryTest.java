package at.htl.workloads.audioItem;

import at.htl.error.InvalidRequestException;
import at.htl.model.audioItem.UnusedAudioItemDTO;
import at.htl.model.listeningSession.CreateListeningSessionRequest;
import at.htl.workloads.listeningSession.ListeningSessionService;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class UnusedAudioItemsQueryTest {

    private static final LocalDate NOVEMBER_START = LocalDate.of(2026, 11, 1);
    private static final LocalDate NOVEMBER_END = LocalDate.of(2026, 11, 30);
    private static final List<String> SEEDED_TITLES = List.of("Bungalow", "Maschin", "Bologna", "Luzia",
            "Get Lucky", "Instant Crush", "One More Time", "Warum JPA?", "Joins verstehen");

    @Inject
    AudioItemService audioItemService;

    @Inject
    ListeningSessionService sessionService;

    @Test
    void returnsOnlyItemsWithoutSessionInRange() {
        var titles = titles(audioItemService.getUnused(NOVEMBER_START, NOVEMBER_END)).stream()
                .filter(SEEDED_TITLES::contains)
                .toList();

        assertEquals(List.of("One More Time", "Joins verstehen", "Luzia"), titles);
    }

    @Test
    void outerJoinKeepsItemNeverPlayedAndNeverPlaylisted() {
        var item = find(audioItemService.getUnused(NOVEMBER_START, NOVEMBER_END), "One More Time");

        assertEquals(0L, item.playlistCount());
        assertEquals("Daft Punk", item.artistName());
        assertEquals(320, item.durationSeconds());
    }

    @Test
    void countsPlaylistEntriesOfUnusedItem() {
        var item = find(audioItemService.getUnused(NOVEMBER_START, NOVEMBER_END), "Luzia");

        assertEquals(1L, item.playlistCount());
    }

    @Test
    void sessionOutsideRangeDoesNotCount() {
        var day = LocalDate.of(2026, 11, 2);
        var titles = titles(audioItemService.getUnused(day, day));

        assertFalse(titles.contains("Bungalow"));
        assertTrue(titles.contains("Maschin"));
    }

    @Test
    void rangeWithoutAnySessionReturnsEveryItem() {
        var unused = audioItemService.getUnused(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

        assertEquals(audioItemService.getAllAudioItems().size(), unused.size());
    }

    @Test
    void upperBoundIncludesTheWholeLastDay() {
        var day = LocalDate.of(2026, 11, 5);
        var titles = titles(audioItemService.getUnused(day, day));

        assertFalse(titles.contains("Warum JPA?"));
    }

    @Test
    @TestTransaction
    void noHitsWhenEveryItemWasPlayed() {
        var startedAt = LocalDateTime.of(2026, 6, 1, 12, 0);
        for (var item : audioItemService.getAllAudioItems()) {
            sessionService.recordSession(new CreateListeningSessionRequest(1L, 1L, item.id(), startedAt, 1));
        }

        var day = startedAt.toLocalDate();
        assertTrue(audioItemService.getUnused(day, day).isEmpty());
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(InvalidRequestException.class,
                () -> audioItemService.getUnused(NOVEMBER_END, NOVEMBER_START));
        assertThrows(InvalidRequestException.class,
                () -> audioItemService.getUnused(null, NOVEMBER_END));
        assertThrows(InvalidRequestException.class,
                () -> audioItemService.getUnused(NOVEMBER_START, null));
    }

    private static List<String> titles(List<UnusedAudioItemDTO> items) {
        return items.stream().map(UnusedAudioItemDTO::title).toList();
    }

    private static UnusedAudioItemDTO find(List<UnusedAudioItemDTO> items, String title) {
        return items.stream()
                .filter(item -> item.title().equals(title))
                .findFirst()
                .orElseThrow(() -> new AssertionError(title + " missing"));
    }
}
