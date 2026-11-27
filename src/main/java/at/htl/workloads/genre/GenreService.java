package at.htl.workloads.genre;

import at.htl.model.genre.CreateGenreRequest;
import at.htl.model.genre.GenreDTO;

import java.util.List;

public interface GenreService {

    GenreDTO createGenre(CreateGenreRequest request);

    List<GenreDTO> getAllGenres();
}
