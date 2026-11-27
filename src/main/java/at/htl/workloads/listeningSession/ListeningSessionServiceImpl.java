package at.htl.workloads.listeningSession;

import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import at.htl.model.listeningSession.CreateListeningSessionRequest;
import at.htl.model.listeningSession.ListeningSessionDTO;
import at.htl.workloads.audioItem.AudioItemRepository;
import at.htl.workloads.device.DeviceRepository;
import at.htl.workloads.user.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ListeningSessionServiceImpl implements ListeningSessionService {

    private final ListeningSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final AudioItemRepository audioItemRepository;
    private final ListeningSessionMapper sessionMapper;

    public ListeningSessionServiceImpl(ListeningSessionRepository sessionRepository,
                                       UserRepository userRepository,
                                       DeviceRepository deviceRepository,
                                       AudioItemRepository audioItemRepository,
                                       ListeningSessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.audioItemRepository = audioItemRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    @Transactional
    public ListeningSessionDTO recordSession(CreateListeningSessionRequest request) {
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException("User " + request.userId() + " does not exist"));
        var device = deviceRepository.findById(request.deviceId())
                .orElseThrow(() -> new NotFoundException("Device " + request.deviceId() + " does not exist"));
        var audioItem = audioItemRepository.findById(request.audioItemId())
                .orElseThrow(() -> new NotFoundException("Audio item " + request.audioItemId() + " does not exist"));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new InvalidRequestException("Device " + device.getId() + " does not belong to user " + user.getId());
        }
        if (request.listenedSeconds() > audioItem.getDurationSeconds()) {
            throw new InvalidRequestException("Listened seconds exceed the duration of audio item " + audioItem.getId());
        }

        var session = new ListeningSession();
        session.setUser(user);
        session.setDevice(device);
        session.setAudioItem(audioItem);
        session.setStartedAt(request.startedAt());
        session.setListenedSeconds(request.listenedSeconds());
        sessionRepository.add(session);

        return sessionMapper.toDto(session);
    }

    @Override
    public List<ListeningSessionDTO> getSessions(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User " + userId + " does not exist");
        }
        return sessionRepository.findByUser(userId).stream()
                .map(sessionMapper::toDto)
                .toList();
    }
}
