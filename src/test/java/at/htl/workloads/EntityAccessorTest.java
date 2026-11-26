package at.htl.workloads;

import at.htl.workloads.album.Album;
import at.htl.workloads.device.Device;
import at.htl.workloads.favorite.Favorite;
import at.htl.workloads.listeningSession.ListeningSession;
import at.htl.workloads.playlist.Playlist;
import at.htl.workloads.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityAccessorTest {

    @Test
    void albumIdRoundTrips() {
        var entity = new Album();
        assertNull(entity.getId());
        entity.setId(11L);
        assertEquals(11L, entity.getId());
    }

    @Test
    void deviceIdRoundTrips() {
        var entity = new Device();
        assertNull(entity.getId());
        entity.setId(12L);
        assertEquals(12L, entity.getId());
    }

    @Test
    void favoriteIdRoundTrips() {
        var entity = new Favorite();
        assertNull(entity.getId());
        entity.setId(13L);
        assertEquals(13L, entity.getId());
    }

    @Test
    void listeningSessionIdRoundTrips() {
        var entity = new ListeningSession();
        assertNull(entity.getId());
        entity.setId(14L);
        assertEquals(14L, entity.getId());
    }

    @Test
    void playlistIdRoundTrips() {
        var entity = new Playlist();
        assertNull(entity.getId());
        entity.setId(15L);
        assertEquals(15L, entity.getId());
    }

    @Test
    void userIdRoundTrips() {
        var entity = new User();
        assertNull(entity.getId());
        entity.setId(16L);
        assertEquals(16L, entity.getId());
    }
}
