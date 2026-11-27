package at.htl.workloads.artist;

import at.htl.model.artist.ArtistDTO;
import at.htl.model.artist.CreateArtistRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    @Override
    @Transactional
    public ArtistDTO createArtist(CreateArtistRequest request) {
        var artist = new Artist();
        artist.setName(request.name());
        artist.setCountry(request.country());
        artistRepository.add(artist);

        return artistMapper.toDto(artist);
    }

    @Override
    public List<ArtistDTO> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(artistMapper::toDto)
                .toList();
    }

}
