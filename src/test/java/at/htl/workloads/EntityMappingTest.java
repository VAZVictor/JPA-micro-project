package at.htl.workloads;

import at.htl.workloads.album.Album;
import at.htl.workloads.artist.Artist;
import at.htl.workloads.audioItem.AudioItem;
import at.htl.workloads.audioItem.PodcastEpisode;
import at.htl.workloads.audioItem.Track;
import at.htl.workloads.device.Device;
import at.htl.workloads.favorite.Favorite;
import at.htl.workloads.listeningSession.ListeningSession;
import at.htl.workloads.playlist.Playlist;
import at.htl.workloads.playlist.PlaylistEntry;
import at.htl.workloads.user.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityMappingTest {

    @Test
    void persistentClassesAreEntitiesButGenreIsNot() {
        var entityTypes = List.of(Album.class, Artist.class, AudioItem.class, Track.class,
                PodcastEpisode.class, Device.class, Favorite.class, ListeningSession.class,
                Playlist.class, PlaylistEntry.class, User.class);

        for (var entityType : entityTypes) {
            assertTrue(entityType.isAnnotationPresent(Entity.class),
                    () -> entityType.getSimpleName() + " must be an entity");
        }
        assertFalse(Genre.class.isAnnotationPresent(Entity.class));
    }

    @Test
    void standaloneEntitiesAndInheritanceRootUseGeneratedLongIdentityIds()
            throws NoSuchFieldException, NoSuchMethodException {
        var idTypes = List.of(Album.class, Artist.class, AudioItem.class, Device.class,
                Favorite.class, ListeningSession.class, Playlist.class, User.class);

        for (var idType : idTypes) {
            assertIdentityId(idType);
        }

        assertFalse(hasDeclaredId(Track.class), "Track inherits AudioItem's id");
        assertFalse(hasDeclaredId(PodcastEpisode.class), "PodcastEpisode inherits AudioItem's id");
        assertEquals(Long.class, Track.class.getMethod("getId").getReturnType());
        assertEquals(Long.class, PodcastEpisode.class.getMethod("getId").getReturnType());
    }

    @Test
    void joinedInheritanceStrategyIsDeclaredOnAudioItem() {
        var inheritance = AudioItem.class.getAnnotation(Inheritance.class);
        assertNotNull(inheritance, "The inheritance mapping belongs to the root entity");
        assertEquals(InheritanceType.JOINED, inheritance.strategy());
    }

    @Test
    void playlistEntryHasACompositeKeyOfPlaylistIdAndPosition()
            throws NoSuchFieldException, NoSuchMethodException {
        var embeddedIdField = Arrays.stream(PlaylistEntry.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(EmbeddedId.class))
                .findFirst();

        Class<?> keyType;
        if (embeddedIdField.isPresent()) {
            keyType = embeddedIdField.get().getType();
            assertTrue(keyType.isAnnotationPresent(Embeddable.class),
                    "An embedded key class must be embeddable");
        } else {
            var idClass = PlaylistEntry.class.getAnnotation(IdClass.class);
            assertNotNull(idClass, "Use either an embedded id or an id class for the composite key");
            if (idClass == null) {
                return;
            }
            keyType = idClass.value();
            assertEntityIdField("playlistId", Long.class);
            assertEntityIdField("position", Integer.class);
        }

        assertEquals(Long.class, keyType.getDeclaredField("playlistId").getType());
        assertEquals(Integer.class, keyType.getDeclaredField("position").getType());
        assertTrue(Serializable.class.isAssignableFrom(keyType));
        assertNotNull(keyType.getDeclaredConstructor());
        assertNotNull(keyType.getDeclaredMethod("equals", Object.class));
        assertNotNull(keyType.getDeclaredMethod("hashCode"));
    }

    private static boolean hasDeclaredId(Class<?> type) {
        try {
            type.getDeclaredField("id");
            return true;
        } catch (NoSuchFieldException ignored) {
            return false;
        }
    }

    private static void assertEntityIdField(String name, Class<?> expectedType) throws NoSuchFieldException {
        Field field = PlaylistEntry.class.getDeclaredField(name);
        assertEquals(expectedType, field.getType(), name + " type");
        assertNotNull(field.getAnnotation(Id.class), name + " @Id");
    }

    private static void assertIdentityId(Class<?> type) throws NoSuchFieldException {
        Field id = type.getDeclaredField("id");
        assertEquals(Long.class, id.getType(), type.getSimpleName() + " id type");
        assertNotNull(id.getAnnotation(Id.class), type.getSimpleName() + " @Id");
        var generatedValue = id.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValue, type.getSimpleName() + " @GeneratedValue");
        assertEquals(GenerationType.IDENTITY, generatedValue.strategy(),
                type.getSimpleName() + " ID generation strategy");
    }
}
