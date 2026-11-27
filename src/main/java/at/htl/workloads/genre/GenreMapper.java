package at.htl.workloads.genre;

import at.htl.model.genre.GenreDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenreMapper {

    public GenreDTO toDto(Genre genre) {
        return new GenreDTO(
                genre.getId(),
                genre.getName()
        );
    }
}
