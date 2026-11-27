package at.htl.workloads.playlist;

import at.htl.model.playlist.PlaylistOverviewDTO;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository {

    Optional<Playlist> findById(Long id);

    Optional<Playlist> findByIdWithEntries(Long id);

    List<PlaylistOverviewDTO> getOverviews(Long userId);

    Optional<PlaylistOverviewDTO> getOverview(Long id);

    void add(Playlist playlist);

    void remove(Playlist playlist);

    void flush();
}
