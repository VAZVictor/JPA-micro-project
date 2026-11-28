package at.htl.workloads;

import at.htl.workloads.playlist.PlaylistService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class PlaylistServiceTest {

    @Inject
    PlaylistService playlistService;

    @Test
    @Transactional
    void shouldAccessPlaylistService() {
        assertNotNull(playlistService);
    }
}