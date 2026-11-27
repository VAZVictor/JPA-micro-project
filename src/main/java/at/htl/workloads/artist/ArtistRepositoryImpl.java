package at.htl.workloads.artist;

import at.htl.model.artist.ArtistListeningDTO;
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
    public List<ArtistListeningDTO> findTopArtists(long minSeconds, int page, int size) {
        return entityManager.createQuery(
                        """
                        select new at.htl.model.artist.ArtistListeningDTO(
                            ar.id, ar.name, sum(s.listenedSeconds), count(s.id))
                        from ListeningSession s
                        join s.audioItem a
                        join a.artist ar
                        group by ar.id, ar.name
                        having sum(s.listenedSeconds) >= :minSeconds
                        order by sum(s.listenedSeconds) desc, ar.name
                        """,
                        ArtistListeningDTO.class
                )
                .setParameter("minSeconds", minSeconds)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void add(Artist artist) {
        entityManager.persist(artist);
    }
}
