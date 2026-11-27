package at.htl.model.audioItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreatePodcastEpisodeRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        Integer durationSeconds,

        @NotNull(message = "Host is required")
        Long hostId,

        @NotBlank(message = "Show name is required")
        @Size(max = 200, message = "Show name must be at most 200 characters")
        String showName,

        @Positive(message = "Episode number must be positive")
        Integer episodeNumber,

        LocalDate publishedAt,

        List<Long> genreIds
) {
}
