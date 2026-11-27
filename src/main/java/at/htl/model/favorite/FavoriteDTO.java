package at.htl.model.favorite;

import java.time.LocalDateTime;

public record FavoriteDTO(
        Long id,
        Long userId,
        Long audioItemId,
        String title,
        String artistName,
        LocalDateTime createdAt
) {
}
