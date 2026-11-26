package at.htl.workloads;

import at.htl.workloads.artist.Artist;
import at.htl.workloads.audioItem.Track;
import at.htl.workloads.playlist.Playlist;
import at.htl.workloads.playlist.PlaylistEntry;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelationshipMappingTest {

    @Test
    void artistTrackRelationshipHasOneOwningSideAndLoadsLazily() throws NoSuchFieldException {
        var tracksField = Artist.class.getDeclaredField("tracks");
        var oneToMany = tracksField.getAnnotation(OneToMany.class);
        assertNotNull(oneToMany);
        assertEquals("artist", oneToMany.mappedBy());
        assertEquals(FetchType.LAZY, oneToMany.fetch());
        assertEquals(0, oneToMany.cascade().length,
                "Artist and tracks have independent lifecycles");
        assertFalse(oneToMany.orphanRemoval());
        assertFalse(tracksField.isAnnotationPresent(JoinColumn.class),
                "The inverse side must not own the foreign key");

        var artistField = Track.class.getDeclaredField("artist");
        var manyToOne = artistField.getAnnotation(ManyToOne.class);
        assertNotNull(manyToOne);
        assertEquals(FetchType.LAZY, manyToOne.fetch());
        var joinColumn = artistField.getAnnotation(JoinColumn.class);
        assertNotNull(joinColumn, "Track owns the foreign key to Artist");
        assertEquals("artist_id", joinColumn.name());
    }

    @Test
    void playlistHasOrderedMasterDetailWithCascadeAndOrphanRemoval() {
        var entriesField = Arrays.stream(Playlist.class.getDeclaredFields())
                .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                .filter(RelationshipMappingTest::containsPlaylistEntry)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Playlist must expose its PlaylistEntry details"));

        var oneToMany = entriesField.getAnnotation(OneToMany.class);
        assertNotNull(oneToMany, "The playlist detail collection must be a one-to-many relation");
        assertEquals("playlist", oneToMany.mappedBy());
        assertEquals(FetchType.LAZY, oneToMany.fetch());
        assertTrue(Arrays.asList(oneToMany.cascade()).contains(CascadeType.ALL));
        assertTrue(oneToMany.orphanRemoval());

        var orderBy = entriesField.getAnnotation(OrderBy.class);
        assertNotNull(orderBy, "Playlist details must be ordered by position");
        assertEquals("position", orderBy.value());
    }

    @Test
    void playlistReferenceIsRequiredAndLazy() throws NoSuchFieldException {
        assertRequiredLazyReference("playlist");
    }

    @Test
    void audioItemReferenceIsRequiredAndLazy() throws NoSuchFieldException {
        assertRequiredLazyReference("audioItem");
    }

    @Test
    void onlyPlaylistReferenceContributesToTheCompositeKey() throws NoSuchFieldException {
        var playlistMapsId = PlaylistEntry.class.getDeclaredField("playlist").getAnnotation(MapsId.class);
        assertNotNull(playlistMapsId);
        assertEquals("playlistId", playlistMapsId.value());

        var audioItemMapsId = PlaylistEntry.class.getDeclaredField("audioItem").getAnnotation(MapsId.class);
        assertNull(audioItemMapsId,
                "audioItem is not part of the agreed playlistId + position key");
    }

    private static void assertRequiredLazyReference(String fieldName) throws NoSuchFieldException {
        var field = PlaylistEntry.class.getDeclaredField(fieldName);
        var manyToOne = field.getAnnotation(ManyToOne.class);
        assertNotNull(manyToOne, fieldName + " must be a many-to-one reference");
        assertEquals(FetchType.LAZY, manyToOne.fetch(), fieldName + " must load lazily");
        assertFalse(manyToOne.optional(), fieldName + " is required");

        var joinColumn = field.getAnnotation(JoinColumn.class);
        assertNotNull(joinColumn, fieldName + " must own its foreign key");
        assertFalse(joinColumn.nullable(), fieldName + " foreign key must be non-null");
    }

    private static boolean containsPlaylistEntry(Field field) {
        if (!(field.getGenericType() instanceof ParameterizedType parameterizedType)) {
            return false;
        }
        return Arrays.asList(parameterizedType.getActualTypeArguments()).contains(PlaylistEntry.class);
    }
}
