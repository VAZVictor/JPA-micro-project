package at.htl.workloads.playlist;

import java.util.Optional;

public interface PlaylistRepository {

    Optional<Playlist> findById(Long id);

    Optional<Playlist> findByIdWithEntries(Long id);

    void add(Playlist playlist);

    void remove(Playlist playlist);

    void flush();
}
