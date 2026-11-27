package at.htl.workloads.playlist;

import at.htl.model.playlist.PlaylistOverviewDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlaylistRepositoryImpl implements PlaylistRepository {

    private static final String OVERVIEW_SELECT = """
            select new at.htl.model.playlist.PlaylistOverviewDTO(
                p.id, p.name, u.username, count(a.id), coalesce(sum(a.durationSeconds), 0L), max(e.addedAt))
            from Playlist p
            join p.user u
            left join p.entries e
            left join e.audioItem a
            """;

    private static final String OVERVIEW_GROUP = """
            group by p.id, p.name, u.username
            order by p.name
            """;

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
    public List<PlaylistOverviewDTO> getOverviews(Long userId) {
        var jpql = new StringBuilder(OVERVIEW_SELECT);
        if (userId != null) {
            jpql.append("where u.id = :userId\n");
        }
        jpql.append(OVERVIEW_GROUP);

        var query = entityManager.createQuery(jpql.toString(), PlaylistOverviewDTO.class);
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        return query.getResultList();
    }

    @Override
    public Optional<PlaylistOverviewDTO> getOverview(Long id) {
        return entityManager.createQuery(
                        OVERVIEW_SELECT + "where p.id = :id\n" + OVERVIEW_GROUP,
                        PlaylistOverviewDTO.class
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
