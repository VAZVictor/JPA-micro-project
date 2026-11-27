package at.htl.workloads.audioItem;

import at.htl.model.audioItem.AudioItemDTO;
import at.htl.model.audioItem.CreatePodcastEpisodeRequest;
import at.htl.model.audioItem.CreateTrackRequest;
import at.htl.model.audioItem.PodcastEpisodeDTO;
import at.htl.model.audioItem.TrackDTO;
import at.htl.model.audioItem.UnusedAudioItemDTO;
import at.htl.model.audioItem.UpdateTrackRequest;

import java.time.LocalDate;
import java.util.List;

public interface AudioItemService {

    List<AudioItemDTO> getAllAudioItems();

    AudioItemDTO getAudioItem(Long id);

    List<UnusedAudioItemDTO> getUnused(LocalDate from, LocalDate to);

    List<TrackDTO> getAllTracks();

    TrackDTO getTrack(Long id);

    TrackDTO createTrack(CreateTrackRequest request);

    TrackDTO updateTrack(Long id, UpdateTrackRequest request);

    void deleteTrack(Long id);

    List<PodcastEpisodeDTO> getAllPodcastEpisodes();

    PodcastEpisodeDTO createPodcastEpisode(CreatePodcastEpisodeRequest request);
}
