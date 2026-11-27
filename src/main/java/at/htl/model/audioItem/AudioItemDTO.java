package at.htl.model.audioItem;

import java.util.List;

public record AudioItemDTO(
        Long id,
        String type,
        String title,
        Integer durationSeconds,
        Long artistId,
        String artistName,
        List<String> genres
) {
}
