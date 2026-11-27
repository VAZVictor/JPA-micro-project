package at.htl.model.playlist;

import java.time.LocalDateTime;

public record PlaylistOverviewDTO(
        Long playlistId,
        String name,
        String username,
        Long entryCount,
        Long totalDurationSeconds,
        LocalDateTime lastAddedAt
) {
}
