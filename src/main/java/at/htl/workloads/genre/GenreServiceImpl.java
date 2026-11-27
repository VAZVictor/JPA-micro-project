package at.htl.workloads.genre;

import at.htl.error.DuplicateException;
import at.htl.model.genre.CreateGenreRequest;
import at.htl.model.genre.GenreDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    @Transactional
    public GenreDTO createGenre(CreateGenreRequest request) {
        if (genreRepository.findByName(request.name()).isPresent()) {
            throw new DuplicateException("Genre '" + request.name() + "' already exists");
        }

        var genre = new Genre();
        genre.setName(request.name());
        genreRepository.add(genre);

        return genreMapper.toDto(genre);
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .toList();
    }
}
