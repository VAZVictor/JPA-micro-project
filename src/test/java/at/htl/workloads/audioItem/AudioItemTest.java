package at.htl.workloads.audioItem;

import at.htl.workloads.artist.Artist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AudioItemTest {

    @Test
    void inheritedIdAccessorsRoundTripForBothSubtypes() {
        var track = new Track();
        var episode = new PodcastEpisode();

        assertNull(track.getId());
        assertNull(episode.getId());
        track.setId(31L);
        episode.setId(32L);

        assertEquals(31L, track.getId());
        assertEquals(32L, episode.getId());
    }

    @Test
    void trackArtistAccessorsRoundTrip() {
        var track = new Track();
        var artist = new Artist("Artist");

        assertNull(track.getArtist());
        track.setArtist(artist);

        assertSame(artist, track.getArtist());
    }

    @Test
    void entitiesWithSameIdInTheInheritanceHierarchyAreEqual() {
        var track = new Track();
        var episode = new PodcastEpisode();
        track.setId(42L);
        episode.setId(42L);

        assertEquals(track, episode);
        assertEquals(track.hashCode(), episode.hashCode());
    }

    @Test
    void entitiesWithDifferentPersistentIdsAreNotEqual() {
        var track = new Track();
        var episode = new PodcastEpisode();
        track.setId(42L);
        episode.setId(43L);

        assertNotEquals(track, episode);
    }

    @Test
    void transientEntitiesWithNoIdAreOnlyEqualToThemselves() {
        var first = new Track();
        var second = new Track();

        assertEquals(first, first);
        assertNotEquals(first, second);
    }
}
