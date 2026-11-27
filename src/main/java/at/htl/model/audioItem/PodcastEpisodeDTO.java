package at.htl.model.audioItem;

import java.time.LocalDate;
import java.util.List;

public record PodcastEpisodeDTO(
        Long id,
        String title,
        Integer durationSeconds,
        Long hostId,
        String hostName,
        String showName,
        Integer episodeNumber,
        LocalDate publishedAt,
        List<String> genres
) {
}
