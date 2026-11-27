package at.htl.model.playlist;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistDTO(
        Long id,
        String name,
        String description,
        Long userId,
        String username,
        LocalDateTime createdAt,
        List<PlaylistEntryDTO> entries
) {
}
