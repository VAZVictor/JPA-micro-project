package at.htl.model.playlist;

import jakarta.validation.constraints.NotNull;

public record AddEntryRequest(
        @NotNull(message = "Audio item is required")
        Long audioItemId
) {
}
