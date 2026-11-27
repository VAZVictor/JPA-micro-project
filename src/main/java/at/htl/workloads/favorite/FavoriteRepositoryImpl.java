package at.htl.workloads.favorite;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final EntityManager entityManager;

    public FavoriteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Favorite> findByUser(Long userId) {
        return entityManager.createQuery(
                        """
                        select f from Favorite f
                        join fetch f.audioItem a
                        join fetch a.artist
                        where f.user.id = :userId
                        order by f.createdAt desc
                        """,
                        Favorite.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<Favorite> findByUserAndAudioItem(Long userId, Long audioItemId) {
        return entityManager.createQuery(
                        """
                        select f from Favorite f
                        where f.user.id = :userId and f.audioItem.id = :audioItemId
                        """,
                        Favorite.class
                )
                .setParameter("userId", userId)
                .setParameter("audioItemId", audioItemId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void add(Favorite favorite) {
        entityManager.persist(favorite);
    }

    @Override
    public void remove(Favorite favorite) {
        entityManager.remove(favorite);
    }
}
