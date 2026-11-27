package at.htl.workloads.album;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository {

    List<Album> findAll();

    Optional<Album> findById(Long id);

    void add(Album album);
}
