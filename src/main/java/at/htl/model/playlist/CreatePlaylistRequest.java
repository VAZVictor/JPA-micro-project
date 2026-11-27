package at.htl.model.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePlaylistRequest(
        @NotNull(message = "User is required")
        Long userId,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        List<@NotNull(message = "Audio item id must not be null") Long> audioItemIds
) {
}
