package at.htl.workloads.artist;

import at.htl.error.InvalidRequestException;
import at.htl.model.artist.ArtistDTO;
import at.htl.model.artist.ArtistListeningDTO;
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

    @Override
    public List<ArtistListeningDTO> getTopArtists(Long minSeconds, Integer page, Integer size) {
        long threshold = minSeconds == null ? 0 : minSeconds;
        int pageIndex = page == null ? 0 : page;
        int pageSize = size == null ? 10 : size;
        if (threshold < 0) {
            throw new InvalidRequestException("minSeconds must not be negative");
        }
        if (pageIndex < 0 || pageSize < 1 || pageSize > 100) {
            throw new InvalidRequestException("page must be >= 0 and size between 1 and 100");
        }
        return artistRepository.findTopArtists(threshold, pageIndex, pageSize);
    }
}
