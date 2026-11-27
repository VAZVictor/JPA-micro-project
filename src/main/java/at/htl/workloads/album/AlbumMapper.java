package at.htl.workloads.album;

import at.htl.model.album.AlbumDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlbumMapper {

    public AlbumDTO toDto(Album album) {
        return new AlbumDTO(
                album.getId(),
                album.getTitle(),
                album.getReleaseYear(),
                album.getArtist().getId(),
                album.getArtist().getName()
        );
    }
}
