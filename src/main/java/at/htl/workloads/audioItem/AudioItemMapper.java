package at.htl.workloads.audioItem;

import at.htl.model.audioItem.AudioItemDTO;
import at.htl.model.audioItem.PodcastEpisodeDTO;
import at.htl.model.audioItem.TrackDTO;
import at.htl.workloads.genre.Genre;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AudioItemMapper {

    public AudioItemDTO toDto(AudioItem audioItem) {
        return new AudioItemDTO(
                audioItem.getId(),
                typeOf(audioItem),
                audioItem.getTitle(),
                audioItem.getDurationSeconds(),
                audioItem.getArtist().getId(),
                audioItem.getArtist().getName(),
                genreNames(audioItem)
        );
    }

    public TrackDTO toTrackDto(Track track) {
        var album = track.getAlbum();
        return new TrackDTO(
                track.getId(),
                track.getTitle(),
                track.getDurationSeconds(),
                track.getArtist().getId(),
                track.getArtist().getName(),
                album == null ? null : album.getId(),
                album == null ? null : album.getTitle(),
                track.getTrackNumber(),
                genreNames(track)
        );
    }

    public PodcastEpisodeDTO toPodcastEpisodeDto(PodcastEpisode episode) {
        return new PodcastEpisodeDTO(
                episode.getId(),
                episode.getTitle(),
                episode.getDurationSeconds(),
                episode.getArtist().getId(),
                episode.getArtist().getName(),
                episode.getShowName(),
                episode.getEpisodeNumber(),
                episode.getPublishedAt(),
                genreNames(episode)
        );
    }

    private String typeOf(AudioItem audioItem) {
        return switch (audioItem) {
            case Track _ -> "TRACK";
            case PodcastEpisode _ -> "PODCAST_EPISODE";
            default -> "UNKNOWN";
        };
    }

    private List<String> genreNames(AudioItem audioItem) {
        return audioItem.getGenres().stream()
                .map(Genre::getName)
                .sorted()
                .toList();
    }
}
