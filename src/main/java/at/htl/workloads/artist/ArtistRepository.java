package at.htl.workloads.artist;

import at.htl.model.artist.ArtistListeningDTO;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {

    List<Artist> findAll();

    Optional<Artist> findById(Long id);

    List<ArtistListeningDTO> findTopArtists(long minSeconds, int page, int size);

    void add(Artist artist);
}
