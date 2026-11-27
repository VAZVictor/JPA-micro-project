package at.htl.workloads.playlist;

import at.htl.model.playlist.AddEntryRequest;
import at.htl.model.playlist.CreatePlaylistRequest;
import at.htl.model.playlist.PlaylistDTO;
import at.htl.model.playlist.ReorderPlaylistRequest;
import at.htl.model.playlist.UpdatePlaylistRequest;

public interface PlaylistService {

    PlaylistDTO getPlaylist(Long id);

    PlaylistDTO createPlaylist(CreatePlaylistRequest request);

    PlaylistDTO updatePlaylist(Long id, UpdatePlaylistRequest request);

    PlaylistDTO addEntry(Long id, AddEntryRequest request);

    PlaylistDTO removeEntry(Long id, Integer position);

    PlaylistDTO reorder(Long id, ReorderPlaylistRequest request);

    void deletePlaylist(Long id);
}
