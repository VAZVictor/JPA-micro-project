package at.htl.model.playlist;

import java.time.LocalDateTime;

public record PlaylistEntryDTO(
        Integer position,
        Long audioItemId,
        String title,
        String artistName,
        Integer durationSeconds,
        LocalDateTime addedAt
) {
}
