package at.htl.workloads.artist;

import at.htl.model.artist.ArtistDTO;
import at.htl.model.artist.CreateArtistRequest;

import java.util.List;

public interface ArtistService {

    ArtistDTO createArtist(CreateArtistRequest request);

    List<ArtistDTO> getAllArtists();

}
