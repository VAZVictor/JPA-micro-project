package at.htl.workloads.audioItem;

import java.util.List;
import java.util.Optional;

public interface AudioItemRepository {

    List<AudioItem> findAll();

    Optional<AudioItem> findById(Long id);

    List<Track> findAllTracks();

    Optional<Track> findTrackById(Long id);

    List<PodcastEpisode> findAllPodcastEpisodes();

    boolean isReferenced(Long id);

    void add(AudioItem audioItem);

    void remove(AudioItem audioItem);
}
