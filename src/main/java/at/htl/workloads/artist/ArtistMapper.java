package at.htl.workloads.artist;

import at.htl.model.artist.ArtistDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistMapper {

    public ArtistDTO toDto(Artist artist) {
        return new ArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getCountry()
        );
    }
}
