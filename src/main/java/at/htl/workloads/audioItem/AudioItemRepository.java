package at.htl.workloads.audioItem;

import at.htl.model.audioItem.UnusedAudioItemDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AudioItemRepository {

    List<AudioItem> findAll();

    Optional<AudioItem> findById(Long id);

    List<Track> findAllTracks();

    Optional<Track> findTrackById(Long id);

    List<PodcastEpisode> findAllPodcastEpisodes();

    List<UnusedAudioItemDTO> findUnused(LocalDateTime from, LocalDateTime to);

    boolean isReferenced(Long id);

    void add(AudioItem audioItem);

    void remove(AudioItem audioItem);
}
