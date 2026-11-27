package at.htl.workloads.album;

import at.htl.model.album.AlbumDTO;
import at.htl.model.album.CreateAlbumRequest;

import java.util.List;

public interface AlbumService {

    AlbumDTO createAlbum(CreateAlbumRequest request);

    List<AlbumDTO> getAllAlbums();
}
