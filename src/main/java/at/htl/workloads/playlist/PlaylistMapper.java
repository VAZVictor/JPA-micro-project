package at.htl.workloads.playlist;

import at.htl.model.playlist.PlaylistDTO;
import at.htl.model.playlist.PlaylistEntryDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;

@ApplicationScoped
public class PlaylistMapper {

    public PlaylistDTO toDto(Playlist playlist) {
        return new PlaylistDTO(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getUser().getId(),
                playlist.getUser().getUsername(),
                playlist.getCreatedAt(),
                playlist.getEntries().stream()
                        .sorted(Comparator.comparing(entry -> entry.getId().getPosition()))
                        .map(this::toEntryDto)
                        .toList()
        );
    }

    public PlaylistEntryDTO toEntryDto(PlaylistEntry entry) {
        var audioItem = entry.getAudioItem();
        return new PlaylistEntryDTO(
                entry.getId().getPosition(),
                audioItem.getId(),
                audioItem.getTitle(),
                audioItem.getArtist().getName(),
                audioItem.getDurationSeconds(),
                entry.getAddedAt()
        );
    }
}
