package at.htl.model.audioItem;

import java.util.List;

public record TrackDTO(
        Long id,
        String title,
        Integer durationSeconds,
        Long artistId,
        String artistName,
        Long albumId,
        String albumTitle,
        Integer trackNumber,
        List<String> genres
) {
}
