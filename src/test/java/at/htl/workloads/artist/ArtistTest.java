package at.htl.workloads.artist;

import at.htl.workloads.audioItem.Track;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtistTest {

    @Test
    void noArgConstructorAndNameAccessorsWork() {
        var artist = new Artist();

        assertNull(artist.getId());
        assertNull(artist.getName());
        assertTrue(artist.getTracks().isEmpty());

        artist.setName("Artist");

        assertEquals("Artist", artist.getName());
    }

    @Test
    void idAccessorsRoundTrip() {
        var artist = new Artist();

        assertNull(artist.getId());
        artist.setId(17L);
        assertEquals(17L, artist.getId());
    }

    @Test
    void nameConstructorInitializesTheName() {
        var artist = new Artist("Artist");

        assertEquals("Artist", artist.getName());
    }

    @Test
    void addTrackKeepsBothSidesOfTheRelationshipInSync() {
        var artist = new Artist("Artist");
        var track = new Track();

        artist.addTrack(track);

        assertTrue(artist.getTracks().contains(track));
        assertSame(artist, track.getArtist());
    }

    @Test
    void removeTrackKeepsBothSidesOfTheRelationshipInSync() {
        var artist = new Artist("Artist");
        var track = new Track();
        artist.addTrack(track);

        artist.removeTrack(track);

        assertFalse(artist.getTracks().contains(track));
        assertNull(track.getArtist());
    }

    @Test
    void addingTheSameTrackTwiceDoesNotDuplicateTheRelationship() {
        var artist = new Artist("Artist");
        var track = new Track();

        artist.addTrack(track);
        artist.addTrack(track);

        assertEquals(1, artist.getTracks().size());
        assertSame(artist, track.getArtist());
    }

    @Test
    void movingTrackToAnotherArtistRemovesItFromThePreviousCollection() {
        var previousArtist = new Artist("Previous");
        var nextArtist = new Artist("Next");
        var track = new Track();
        previousArtist.addTrack(track);

        nextArtist.addTrack(track);

        assertFalse(previousArtist.getTracks().contains(track));
        assertTrue(nextArtist.getTracks().contains(track));
        assertSame(nextArtist, track.getArtist());
    }

    @Test
    void removingTrackFromAnotherArtistDoesNotBreakItsExistingRelationship() {
        var owner = new Artist("Owner");
        var other = new Artist("Other");
        var track = new Track();
        owner.addTrack(track);

        other.removeTrack(track);

        assertTrue(owner.getTracks().contains(track));
        assertSame(owner, track.getArtist());
    }

    @Test
    void distinctTransientArtistsAreNotEqual() {
        var first = new Artist("Same name");
        var second = new Artist("Same name");

        assertEquals(first, first);
        assertNotEquals(first, second);
    }

    @Test
    void equalityUsesPersistentIdentityAndHashCodeStaysStable() {
        var first = new Artist("First");
        var second = new Artist("Second");
        var transientArtist = new Artist("First");
        int hashCodeBeforeId = first.hashCode();

        first.setId(7L);
        second.setId(7L);

        assertEquals(first, second);
        assertEquals(hashCodeBeforeId, first.hashCode());
        assertNotEquals(first, transientArtist);

        second.setId(8L);
        assertNotEquals(first, second);
    }
}
