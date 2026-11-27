package at.htl.workloads.user;

import at.htl.error.DuplicateException;
import at.htl.error.NotFoundException;
import at.htl.model.device.CreateDeviceRequest;
import at.htl.model.device.DeviceDTO;
import at.htl.model.favorite.FavoriteDTO;
import at.htl.model.favorite.FavoriteRequest;
import at.htl.model.user.CreateUserRequest;
import at.htl.model.user.UserDTO;
import at.htl.workloads.audioItem.AudioItemRepository;
import at.htl.workloads.device.Device;
import at.htl.workloads.device.DeviceMapper;
import at.htl.workloads.device.DeviceRepository;
import at.htl.workloads.device.DeviceType;
import at.htl.workloads.favorite.Favorite;
import at.htl.workloads.favorite.FavoriteMapper;
import at.htl.workloads.favorite.FavoriteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final FavoriteRepository favoriteRepository;
    private final AudioItemRepository audioItemRepository;
    private final UserMapper userMapper;
    private final DeviceMapper deviceMapper;
    private final FavoriteMapper favoriteMapper;

    public UserServiceImpl(UserRepository userRepository,
                           DeviceRepository deviceRepository,
                           FavoriteRepository favoriteRepository,
                           AudioItemRepository audioItemRepository,
                           UserMapper userMapper,
                           DeviceMapper deviceMapper,
                           FavoriteMapper favoriteMapper) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.favoriteRepository = favoriteRepository;
        this.audioItemRepository = audioItemRepository;
        this.userMapper = userMapper;
        this.deviceMapper = deviceMapper;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DuplicateException("User '" + request.username() + "' already exists");
        }

        var user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.add(user);

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<DeviceDTO> getDevices(Long userId) {
        requireUser(userId);
        return deviceRepository.findByUser(userId).stream()
                .map(deviceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public DeviceDTO addDevice(Long userId, CreateDeviceRequest request) {
        var user = requireUser(userId);

        var device = new Device();
        device.setName(request.name());
        device.setType(DeviceType.valueOf(request.type()));
        device.setUser(user);
        deviceRepository.add(device);

        return deviceMapper.toDto(device);
    }

    @Override
    public List<FavoriteDTO> getFavorites(Long userId) {
        requireUser(userId);
        return favoriteRepository.findByUser(userId).stream()
                .map(favoriteMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public FavoriteDTO addFavorite(Long userId, FavoriteRequest request) {
        var user = requireUser(userId);
        var audioItem = audioItemRepository.findById(request.audioItemId())
                .orElseThrow(() -> new NotFoundException("Audio item " + request.audioItemId() + " does not exist"));
        if (favoriteRepository.findByUserAndAudioItem(userId, audioItem.getId()).isPresent()) {
            throw new DuplicateException("Audio item " + audioItem.getId() + " is already a favorite of user " + userId);
        }

        var favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAudioItem(audioItem);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.add(favorite);

        return favoriteMapper.toDto(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long audioItemId) {
        requireUser(userId);
        var favorite = favoriteRepository.findByUserAndAudioItem(userId, audioItemId)
                .orElseThrow(() -> new NotFoundException(
                        "Audio item " + audioItemId + " is not a favorite of user " + userId));

        favoriteRepository.remove(favorite);
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " does not exist"));
    }
}
