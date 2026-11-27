package at.htl.model.artist;

public record ArtistListeningDTO(
        Long artistId,
        String name,
        Long totalSeconds,
        Long sessionCount
) {
}
