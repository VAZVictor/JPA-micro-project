package at.htl.workloads.playlist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final EntityManager entityManager;

    public PlaylistRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Playlist> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Playlist.class, id));
    }

    @Override
    public Optional<Playlist> findByIdWithEntries(Long id) {
        return entityManager.createQuery(
                        """
                        select distinct p from Playlist p
                        join fetch p.user
                        left join fetch p.entries e
                        left join fetch e.audioItem a
                        left join fetch a.artist
                        where p.id = :id
                        """,
                        Playlist.class
                )
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void add(Playlist playlist) {
        entityManager.persist(playlist);
    }

    @Override
    public void remove(Playlist playlist) {
        entityManager.remove(playlist);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }
}
