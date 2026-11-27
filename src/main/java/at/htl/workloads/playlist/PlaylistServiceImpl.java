package at.htl.workloads.playlist;

import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import at.htl.model.playlist.AddEntryRequest;
import at.htl.model.playlist.CreatePlaylistRequest;
import at.htl.model.playlist.PlaylistDTO;
import at.htl.model.playlist.ReorderPlaylistRequest;
import at.htl.model.playlist.UpdatePlaylistRequest;
import at.htl.workloads.audioItem.AudioItem;
import at.htl.workloads.audioItem.AudioItemRepository;
import at.htl.workloads.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final AudioItemRepository audioItemRepository;
    private final PlaylistMapper playlistMapper;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository,
                               UserRepository userRepository,
                               AudioItemRepository audioItemRepository,
                               PlaylistMapper playlistMapper) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.audioItemRepository = audioItemRepository;
        this.playlistMapper = playlistMapper;
    }

    @Override
    public PlaylistDTO getPlaylist(Long id) {
        return playlistRepository.findByIdWithEntries(id)
                .map(playlistMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Playlist " + id + " does not exist"));
    }

    @Override
    @Transactional
    public PlaylistDTO createPlaylist(CreatePlaylistRequest request) {
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User " + request.userId() + " does not exist"));
        var audioItemIds = request.audioItemIds() == null ? List.<Long>of() : request.audioItemIds();
        var audioItems = audioItemIds.stream()
                .map(this::requireAudioItem)
                .toList();

        var playlist = new Playlist();
        playlist.setName(request.name());
        playlist.setDescription(request.description());
        playlist.setCreatedAt(LocalDateTime.now());
        playlist.setUser(user);
        playlistRepository.add(playlist);

        var now = LocalDateTime.now();
        for (int i = 0; i < audioItems.size(); i++) {
            playlist.getEntries().add(newEntry(playlist, i + 1, audioItems.get(i), now));
        }

        return playlistMapper.toDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO updatePlaylist(Long id, UpdatePlaylistRequest request) {
        var playlist = requirePlaylist(id);

        playlist.setName(request.name());
        playlist.setDescription(request.description());

        return playlistMapper.toDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO addEntry(Long id, AddEntryRequest request) {
        var playlist = requirePlaylist(id);
        var audioItem = requireAudioItem(request.audioItemId());

        int nextPosition = playlist.getEntries().stream()
                .mapToInt(entry -> entry.getId().getPosition())
                .max()
                .orElse(0) + 1;
        playlist.getEntries().add(newEntry(playlist, nextPosition, audioItem, LocalDateTime.now()));

        return playlistMapper.toDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO removeEntry(Long id, Integer position) {
        var playlist = requirePlaylist(id);
        var remaining = sortedEntries(playlist).stream()
                .filter(entry -> !entry.getId().getPosition().equals(position))
                .toList();
        if (remaining.size() == playlist.getEntries().size()) {
            throw new NotFoundException("Playlist " + id + " has no entry at position " + position);
        }

        rewriteEntries(playlist, remaining);

        return playlistMapper.toDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO reorder(Long id, ReorderPlaylistRequest request) {
        var playlist = requirePlaylist(id);
        var byPosition = playlist.getEntries().stream()
                .collect(Collectors.toMap(entry -> entry.getId().getPosition(), Function.identity()));

        var requested = request.positions();
        if (requested.size() != byPosition.size()
                || requested.stream().distinct().count() != requested.size()
                || !byPosition.keySet().containsAll(requested)) {
            throw new InvalidRequestException(
                    "positions must contain every current position " + byPosition.keySet() + " exactly once");
        }

        rewriteEntries(playlist, requested.stream().map(byPosition::get).toList());

        return playlistMapper.toDto(playlist);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long id) {
        playlistRepository.remove(requirePlaylist(id));
    }

    private void rewriteEntries(Playlist playlist, List<PlaylistEntry> orderedEntries) {
        var snapshot = orderedEntries.stream()
                .map(entry -> new EntrySnapshot(entry.getAudioItem(), entry.getAddedAt()))
                .toList();

        playlist.getEntries().clear();
        playlistRepository.flush();

        for (int i = 0; i < snapshot.size(); i++) {
            var item = snapshot.get(i);
            playlist.getEntries().add(newEntry(playlist, i + 1, item.audioItem(), item.addedAt()));
        }
    }

    private PlaylistEntry newEntry(Playlist playlist, int position, AudioItem audioItem, LocalDateTime addedAt) {
        var entry = new PlaylistEntry();
        entry.setId(new PlaylistEntryId(playlist.getId(), position));
        entry.setPlaylist(playlist);
        entry.setAudioItem(audioItem);
        entry.setAddedAt(addedAt);
        return entry;
    }

    private List<PlaylistEntry> sortedEntries(Playlist playlist) {
        return playlist.getEntries().stream()
                .sorted(Comparator.comparing(entry -> entry.getId().getPosition()))
                .toList();
    }

    private Playlist requirePlaylist(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Playlist " + id + " does not exist"));
    }

    private AudioItem requireAudioItem(Long id) {
        return audioItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio item " + id + " does not exist"));
    }

    private record EntrySnapshot(AudioItem audioItem, LocalDateTime addedAt) {
    }
}
