package at.htl.workloads.listeningSession;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ListeningSessionRepositoryImpl implements ListeningSessionRepository {

    private final EntityManager entityManager;

    public ListeningSessionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ListeningSession> findByUser(Long userId) {
        return entityManager.createQuery(
                        """
                        select s from ListeningSession s
                        join fetch s.device
                        join fetch s.audioItem
                        where s.user.id = :userId
                        order by s.startedAt desc
                        """,
                        ListeningSession.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void add(ListeningSession session) {
        entityManager.persist(session);
    }
}
