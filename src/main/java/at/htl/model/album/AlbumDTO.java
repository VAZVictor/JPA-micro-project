package at.htl.model.album;

public record AlbumDTO(
        Long id,
        String title,
        Integer releaseYear,
        Long artistId,
        String artistName
) {
}
