package at.htl.workloads.audioItem;

import at.htl.model.audioItem.UnusedAudioItemDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AudioItemRepositoryImpl implements AudioItemRepository {

    private final EntityManager entityManager;

    public AudioItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<AudioItem> findAll() {
        return entityManager.createQuery(
                        """
                        select distinct a from AudioItem a
                        join fetch a.artist
                        left join fetch a.genres
                        order by a.title
                        """,
                        AudioItem.class
                ).getResultList();
    }

    @Override
    public Optional<AudioItem> findById(Long id) {
        return Optional.ofNullable(entityManager.find(AudioItem.class, id));
    }

    @Override
    public List<Track> findAllTracks() {
        return entityManager.createQuery(
                        """
                        select distinct t from Track t
                        join fetch t.artist
                        left join fetch t.album
                        left join fetch t.genres
                        order by t.title
                        """,
                        Track.class
                ).getResultList();
    }

    @Override
    public Optional<Track> findTrackById(Long id) {
        return Optional.ofNullable(entityManager.find(Track.class, id));
    }

    @Override
    public List<PodcastEpisode> findAllPodcastEpisodes() {
        return entityManager.createQuery(
                        """
                        select distinct p from PodcastEpisode p
                        join fetch p.artist
                        left join fetch p.genres
                        order by p.showName, p.episodeNumber
                        """,
                        PodcastEpisode.class
                ).getResultList();
    }

    @Override
    public List<UnusedAudioItemDTO> findUnused(LocalDateTime from, LocalDateTime to) {
        return entityManager.createQuery(
                        """
                        select new at.htl.model.audioItem.UnusedAudioItemDTO(
                            a.id, a.title, ar.name, a.durationSeconds, count(pe.addedAt))
                        from AudioItem a
                        join a.artist ar
                        left join ListeningSession s
                            on s.audioItem = a and s.startedAt >= :from and s.startedAt < :to
                        left join PlaylistEntry pe on pe.audioItem = a
                        where s.id is null
                        group by a.id, a.title, ar.name, a.durationSeconds
                        order by ar.name, a.title
                        """,
                        UnusedAudioItemDTO.class
                )
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public boolean isReferenced(Long id) {
        var sessions = entityManager.createQuery(
                        "select count(s) from ListeningSession s where s.audioItem.id = :id",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
        var entries = entityManager.createQuery(
                        "select count(e) from PlaylistEntry e where e.audioItem.id = :id",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
        var favorites = entityManager.createQuery(
                        "select count(f) from Favorite f where f.audioItem.id = :id",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
        return sessions + entries + favorites > 0;
    }

    @Override
    public void add(AudioItem audioItem) {
        entityManager.persist(audioItem);
    }

    @Override
    public void remove(AudioItem audioItem) {
        entityManager.remove(audioItem);
    }
}
