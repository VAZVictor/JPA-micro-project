package at.htl.model.audioItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTrackRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        Integer durationSeconds,

        @NotNull(message = "Artist is required")
        Long artistId,

        Long albumId,

        @Positive(message = "Track number must be positive")
        Integer trackNumber,

        List<Long> genreIds
) {
}
