package at.htl.workloads.artist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ArtistRepositoryImpl implements ArtistRepository {

    private final EntityManager entityManager;

    public ArtistRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Artist> findAll() {
        return entityManager.createQuery(
                        "select a from Artist a order by a.name",
                        Artist.class
                ).getResultList();
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Artist.class, id));
    }

    @Override
    public void add(Artist artist) {
        entityManager.persist(artist);
    }
}
