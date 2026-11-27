package at.htl.model.playlist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReorderPlaylistRequest(
        @NotEmpty(message = "Positions are required")
        List<@NotNull(message = "Position must not be null") Integer> positions
) {
}
