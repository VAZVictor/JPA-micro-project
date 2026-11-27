package at.htl.model.audioItem;

public record UnusedAudioItemDTO(
        Long id,
        String title,
        String artistName,
        Integer durationSeconds,
        Long playlistCount
) {
}
