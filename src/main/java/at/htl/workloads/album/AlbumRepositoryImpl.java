package at.htl.workloads.album;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AlbumRepositoryImpl implements AlbumRepository {

    private final EntityManager entityManager;

    public AlbumRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Album> findAll() {
        return entityManager.createQuery(
                        """
                        select al from Album al
                        join fetch al.artist ar
                        order by ar.name, al.releaseYear, al.title
                        """,
                        Album.class
                ).getResultList();
    }

    @Override
    public Optional<Album> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Album.class, id));
    }

    @Override
    public void add(Album album) {
        entityManager.persist(album);
    }
}
