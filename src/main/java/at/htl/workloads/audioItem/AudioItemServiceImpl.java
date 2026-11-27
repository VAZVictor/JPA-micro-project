package at.htl.workloads.audioItem;

import at.htl.error.ConflictException;
import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import at.htl.model.audioItem.AudioItemDTO;
import at.htl.model.audioItem.CreatePodcastEpisodeRequest;
import at.htl.model.audioItem.CreateTrackRequest;
import at.htl.model.audioItem.PodcastEpisodeDTO;
import at.htl.model.audioItem.TrackDTO;
import at.htl.model.audioItem.UnusedAudioItemDTO;
import at.htl.model.audioItem.UpdateTrackRequest;
import at.htl.workloads.album.AlbumRepository;
import at.htl.workloads.artist.ArtistRepository;
import at.htl.workloads.genre.Genre;
import at.htl.workloads.genre.GenreRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class AudioItemServiceImpl implements AudioItemService {

    private final AudioItemRepository audioItemRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final AudioItemMapper audioItemMapper;

    public AudioItemServiceImpl(AudioItemRepository audioItemRepository,
                                ArtistRepository artistRepository,
                                AlbumRepository albumRepository,
                                GenreRepository genreRepository,
                                AudioItemMapper audioItemMapper) {
        this.audioItemRepository = audioItemRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.audioItemMapper = audioItemMapper;
    }

    @Override
    public List<AudioItemDTO> getAllAudioItems() {
        return audioItemRepository.findAll().stream()
                .map(audioItemMapper::toDto)
                .toList();
    }

    @Override
    public AudioItemDTO getAudioItem(Long id) {
        return audioItemRepository.findById(id)
                .map(audioItemMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Audio item " + id + " does not exist"));
    }

    @Override
    public List<UnusedAudioItemDTO> getUnused(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidRequestException("from and to are required");
        }
        if (from.isAfter(to)) {
            throw new InvalidRequestException("from must not be after to");
        }
        return audioItemRepository.findUnused(from.atStartOfDay(), to.plusDays(1).atStartOfDay());
    }

    @Override
    public List<TrackDTO> getAllTracks() {
        return audioItemRepository.findAllTracks().stream()
                .map(audioItemMapper::toTrackDto)
                .toList();
    }

    @Override
    public TrackDTO getTrack(Long id) {
        return audioItemMapper.toTrackDto(requireTrack(id));
    }

    @Override
    @Transactional
    public TrackDTO createTrack(CreateTrackRequest request) {
        var artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new NotFoundException("Artist " + request.artistId() + " does not exist"));

        var track = new Track();
        track.setTitle(request.title());
        track.setDurationSeconds(request.durationSeconds());
        track.setArtist(artist);
        track.setTrackNumber(request.trackNumber());
        if (request.albumId() != null) {
            var album = albumRepository.findById(request.albumId())
                    .orElseThrow(() -> new NotFoundException("Album " + request.albumId() + " does not exist"));
            if (!album.getArtist().getId().equals(artist.getId())) {
                throw new InvalidRequestException("Album " + album.getId() + " belongs to a different artist");
            }
            track.setAlbum(album);
        }
        track.setGenres(resolveGenres(request.genreIds()));
        audioItemRepository.add(track);

        return audioItemMapper.toTrackDto(track);
    }

    @Override
    @Transactional
    public TrackDTO updateTrack(Long id, UpdateTrackRequest request) {
        var track = requireTrack(id);

        track.setTitle(request.title());
        track.setDurationSeconds(request.durationSeconds());
        track.setTrackNumber(request.trackNumber());
        if (request.genreIds() != null) {
            track.getGenres().clear();
            track.getGenres().addAll(resolveGenres(request.genreIds()));
        }

        return audioItemMapper.toTrackDto(track);
    }

    @Override
    @Transactional
    public void deleteTrack(Long id) {
        var track = requireTrack(id);
        if (audioItemRepository.isReferenced(id)) {
            throw new ConflictException("Track " + id + " is still used in sessions, playlists or favorites");
        }
        audioItemRepository.remove(track);
    }

    @Override
    public List<PodcastEpisodeDTO> getAllPodcastEpisodes() {
        return audioItemRepository.findAllPodcastEpisodes().stream()
                .map(audioItemMapper::toPodcastEpisodeDto)
                .toList();
    }

    @Override
    @Transactional
    public PodcastEpisodeDTO createPodcastEpisode(CreatePodcastEpisodeRequest request) {
        var host = artistRepository.findById(request.hostId())
                .orElseThrow(() -> new NotFoundException("Artist " + request.hostId() + " does not exist"));

        var episode = new PodcastEpisode();
        episode.setTitle(request.title());
        episode.setDurationSeconds(request.durationSeconds());
        episode.setArtist(host);
        episode.setShowName(request.showName());
        episode.setEpisodeNumber(request.episodeNumber());
        episode.setPublishedAt(request.publishedAt());
        episode.setGenres(resolveGenres(request.genreIds()));
        audioItemRepository.add(episode);

        return audioItemMapper.toPodcastEpisodeDto(episode);
    }

    private Track requireTrack(Long id) {
        return audioItemRepository.findTrackById(id)
                .orElseThrow(() -> new NotFoundException("Track " + id + " does not exist"));
    }

    private Set<Genre> resolveGenres(List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return new HashSet<>();
        }
        var ids = new HashSet<>(genreIds);
        var genres = genreRepository.findByIds(ids);
        if (genres.size() != ids.size()) {
            throw new NotFoundException("At least one genre of " + ids + " does not exist");
        }
        return new HashSet<>(genres);
    }
}
