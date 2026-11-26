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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityRelationshipTest {

    @Test
    void everyAgreedRelationshipInTheDataModelIsMapped() {
        assertAll("required domain relationships",
                relationshipExists(User.class, Playlist.class),
                relationshipExists(User.class, Favorite.class),
                relationshipExists(User.class, Device.class),
                relationshipExists(User.class, ListeningSession.class),
                relationshipExists(Artist.class, Album.class),
                relationshipExists(Artist.class, AudioItem.class),
                relationshipExists(Album.class, Track.class),
                relationshipExists(AudioItem.class, PlaylistEntry.class),
                relationshipExists(AudioItem.class, ListeningSession.class),
                relationshipExists(AudioItem.class, Favorite.class),
                relationshipExists(Device.class, ListeningSession.class),
                relationshipExists(Playlist.class, PlaylistEntry.class));
    }

    @Test
    void noEntityUsesDirectManyToManyMapping() {
        var entityTypes = List.of(Album.class, Artist.class, AudioItem.class, Track.class,
                PodcastEpisode.class, Device.class, Favorite.class, ListeningSession.class,
                Playlist.class, PlaylistEntry.class, User.class);

        var directManyToMany = entityTypes.stream()
                .flatMap(type -> declaredFields(type).stream())
                .filter(field -> field.isAnnotationPresent(ManyToMany.class))
                .findFirst();

        assertFalse(directManyToMany.isPresent(), () -> directManyToMany
                .map(field -> field.getDeclaringClass().getSimpleName() + "." + field.getName())
                .orElse("A direct @ManyToMany mapping is not allowed"));
    }

    private static Executable relationshipExists(Class<?> first, Class<?> second) {
        return () -> assertTrue(hasAssociation(first, second),
                () -> first.getSimpleName() + " and " + second.getSimpleName()
                        + " must have a JPA relationship");
    }

    private static boolean hasAssociation(Class<?> first, Class<?> second) {
        for (var field : declaredFields(first)) {
            if (isRelationship(field) && matches(relationshipTarget(field), second)) {
                return true;
            }
        }
        for (var field : declaredFields(second)) {
            if (isRelationship(field) && matches(relationshipTarget(field), first)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(Class<?> actual, Class<?> expected) {
        return actual != null
                && (expected.isAssignableFrom(actual) || actual.isAssignableFrom(expected));
    }

    private static boolean isRelationship(Field field) {
        return field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(ManyToMany.class);
    }

    private static Class<?> relationshipTarget(Field field) {
        if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType parameterizedType) {
                var arguments = parameterizedType.getActualTypeArguments();
                if (arguments.length == 1 && arguments[0] instanceof Class<?> target) {
                    return target;
                }
            }
            return null;
        }
        return field.getType();
    }

    private static List<Field> declaredFields(Class<?> type) {
        var fields = new ArrayList<Field>();
        for (var current = type; current != null && current != Object.class;
             current = current.getSuperclass()) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
        }
        return fields;
    }
}
