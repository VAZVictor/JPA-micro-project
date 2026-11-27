package at.htl.workloads.genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findByName(String name);

    List<Genre> findByIds(Collection<Long> ids);

    void add(Genre genre);
}
