package at.htl.workloads.playlist;

import at.htl.workloads.audioItem.Track;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PlaylistEntryTest {

    @Test
    void noArgConstructorAndPropertyAccessorsWork() {
        var entry = new PlaylistEntry();
        var id = new PlaylistEntryId();
        var playlist = new Playlist();
        var track = new Track();

        assertNull(entry.getId());
        assertNull(entry.getPlaylist());
        assertNull(entry.getAudioItem());
        assertEquals(0, entry.getPosition());

        entry.setId(id);
        entry.setPlaylist(playlist);
        entry.setAudioItem(track);
        entry.setPosition(3);

        assertSame(id, entry.getId());
        assertSame(playlist, entry.getPlaylist());
        assertSame(track, entry.getAudioItem());
        assertEquals(3, entry.getPosition());
    }

    @Test
    void equalityAndHashCodeUseTheCompositeId() {
        var firstId = new PlaylistEntryId();
        firstId.setPlaylistId(31L);
        var equalId = new PlaylistEntryId();
        equalId.setPlaylistId(31L);
        var differentId = new PlaylistEntryId();
        differentId.setPlaylistId(32L);

        var first = new PlaylistEntry();
        first.setId(firstId);
        var equal = new PlaylistEntry();
        equal.setId(equalId);
        var different = new PlaylistEntry();
        different.setId(differentId);
        var transientEntry = new PlaylistEntry();

        assertEquals(first, equal);
        assertEquals(first.hashCode(), equal.hashCode());
        assertNotEquals(first, different);
        assertNotEquals(first, transientEntry);
        assertEquals(transientEntry, transientEntry);
    }

    @Test
    void separateTransientEntriesAreNotEqual() {
        var first = new PlaylistEntry();
        var second = new PlaylistEntry();

        assertNotEquals(first, second);
    }

    @Test
    void convenienceConstructorSetsRelationsPositionAndId() {
        var playlist = new Playlist();
        playlist.setId(21L);
        var track = new Track();
        track.setId(22L);

        var entry = new PlaylistEntry(playlist, track, 4);

        assertSame(playlist, entry.getPlaylist());
        assertSame(track, entry.getAudioItem());
        assertEquals(4, entry.getPosition());
        assertNotNull(entry.getId());
    }
}
