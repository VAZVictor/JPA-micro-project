package at.htl.model.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateAlbumRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @Positive(message = "Release year must be positive")
        Integer releaseYear,

        @NotNull(message = "Artist is required")
        Long artistId
) {
}
