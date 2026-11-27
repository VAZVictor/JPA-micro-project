package at.htl.workloads.album;

import at.htl.error.NotFoundException;
import at.htl.model.album.AlbumDTO;
import at.htl.model.album.CreateAlbumRequest;
import at.htl.workloads.artist.ArtistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumMapper albumMapper;

    public AlbumServiceImpl(AlbumRepository albumRepository,
                            ArtistRepository artistRepository,
                            AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumMapper = albumMapper;
    }

    @Override
    @Transactional
    public AlbumDTO createAlbum(CreateAlbumRequest request) {
        var artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new NotFoundException("Artist " + request.artistId() + " does not exist"));

        var album = new Album();
        album.setTitle(request.title());
        album.setReleaseYear(request.releaseYear());
        album.setArtist(artist);
        albumRepository.add(album);

        return albumMapper.toDto(album);
    }

    @Override
    public List<AlbumDTO> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(albumMapper::toDto)
                .toList();
    }
}
