package at.htl.workloads.artist;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {

    List<Artist> findAll();

    Optional<Artist> findById(Long id);

    void add(Artist artist);
}
