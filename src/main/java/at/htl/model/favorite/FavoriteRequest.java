package at.htl.model.favorite;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequest(
        @NotNull(message = "Audio item is required")
        Long audioItemId
) {
}
