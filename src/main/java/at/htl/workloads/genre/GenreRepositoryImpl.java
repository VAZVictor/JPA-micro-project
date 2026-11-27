package at.htl.workloads.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GenreRepositoryImpl implements GenreRepository {

    private final EntityManager entityManager;

    public GenreRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery(
                        "select g from Genre g order by g.name",
                        Genre.class
                ).getResultList();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return entityManager.createQuery(
                        "select g from Genre g where lower(g.name) = lower(:name)",
                        Genre.class
                )
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Genre> findByIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery(
                        "select g from Genre g where g.id in :ids",
                        Genre.class
                )
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public void add(Genre genre) {
        entityManager.persist(genre);
    }
}
