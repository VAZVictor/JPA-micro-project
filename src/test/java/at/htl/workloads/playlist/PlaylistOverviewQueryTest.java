package at.htl.workloads.playlist;

import at.htl.error.NotFoundException;
import at.htl.model.playlist.AddEntryRequest;
import at.htl.model.playlist.PlaylistOverviewDTO;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class PlaylistOverviewQueryTest {

    @Inject
    PlaylistService playlistService;

    @Test
    void computesCountDurationAndLastAddedAt() {
        var overview = playlistService.getOverview(1L);

        assertEquals("Morgenroutine", overview.name());
        assertEquals("elena", overview.username());
        assertEquals(3L, overview.entryCount());
        assertEquals(227L + 205L + 224L, overview.totalDurationSeconds());
        assertEquals(LocalDateTime.of(2026, 10, 10, 7, 3), overview.lastAddedAt());
    }

    @Test
    void emptyPlaylistHasZeroValuesAndNoDate() {
        var overview = playlistService.getOverview(3L);

        assertEquals(0L, overview.entryCount());
        assertEquals(0L, overview.totalDurationSeconds());
        assertNull(overview.lastAddedAt());
    }

    @Test
    void sameItemTwiceIsCountedTwice() {
        var overview = playlistService.getOverview(2L);

        assertEquals(3L, overview.entryCount());
        assertEquals(369L + 337L + 369L, overview.totalDurationSeconds());
    }

    @Test
    void listIsSortedByNameAndFilterableByUser() {
        var all = names(playlistService.getOverviews(null));
        assertEquals(List.of("Fokus", "Leer", "Morgenroutine"), all.stream()
                .filter(name -> List.of("Fokus", "Leer", "Morgenroutine").contains(name))
                .toList());

        assertEquals(List.of("Fokus"), names(playlistService.getOverviews(2L)));
        assertEquals(List.of("Leer"), names(playlistService.getOverviews(3L)));
    }

    @Test
    @TestTransaction
    void reflectsNewEntryImmediately() {
        playlistService.addEntry(3L, new AddEntryRequest(9L));

        var overview = playlistService.getOverview(3L);

        assertEquals(1L, overview.entryCount());
        assertEquals(2100L, overview.totalDurationSeconds());
    }

    @Test
    void unknownPlaylistOrUserIsNotFound() {
        assertThrows(NotFoundException.class, () -> playlistService.getOverview(999_999L));
        assertThrows(NotFoundException.class, () -> playlistService.getOverviews(999_999L));
    }

    private static List<String> names(List<PlaylistOverviewDTO> overviews) {
        return overviews.stream().map(PlaylistOverviewDTO::name).toList();
    }
}
