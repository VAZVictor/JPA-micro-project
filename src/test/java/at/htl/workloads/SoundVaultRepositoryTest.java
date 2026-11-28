package at.htl.workloads;

import at.htl.workloads.audioItem.AudioItemRepository;
import at.htl.workloads.artist.ArtistRepository;
import at.htl.workloads.playlist.PlaylistRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SoundVaultRepositoryTest {

    @Inject
    AudioItemRepository audioItemRepository;

    @Inject
    ArtistRepository artistRepository;

    @Inject
    PlaylistRepository playlistRepository;

    @Test
    void shouldFindUnusedAudioItemsViaOuterJoin() {
        var unusedItems = audioItemRepository.findUnused(null, null);
        assertFalse(unusedItems.isEmpty(), "Es sollten ungenutzte Audio-Items gefunden werden");
        assertTrue(
                unusedItems.stream().anyMatch(item -> item.title().contains("Joins verstehen")),
                "Das ungenutzte Item 'Joins verstehen' muss enthalten sein"
        );
    }

    @Test
    void shouldCalculateTopArtistsWithHavingClause() {
        var topArtists = artistRepository.findTopArtists(300L, 0, 10);
        assertFalse(topArtists.isEmpty(), "Es sollten Top-Artists über dem Schwellenwert gefunden werden");
    }

    @Test
    void shouldProjectPlaylistOverview() {
        var overview = playlistRepository.getOverview(1L);
        assertTrue(overview.isPresent(), "Playlist-Übersicht für ID 1 muss vorhanden sein");
        assertEquals("Morgenroutine", overview.get().name());
    }
}