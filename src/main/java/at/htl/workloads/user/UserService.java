package at.htl.workloads.user;

import at.htl.model.device.CreateDeviceRequest;
import at.htl.model.device.DeviceDTO;
import at.htl.model.favorite.FavoriteDTO;
import at.htl.model.favorite.FavoriteRequest;
import at.htl.model.user.CreateUserRequest;
import at.htl.model.user.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(CreateUserRequest request);

    List<UserDTO> getAllUsers();

    List<DeviceDTO> getDevices(Long userId);

    DeviceDTO addDevice(Long userId, CreateDeviceRequest request);

    List<FavoriteDTO> getFavorites(Long userId);

    FavoriteDTO addFavorite(Long userId, FavoriteRequest request);

    void removeFavorite(Long userId, Long audioItemId);
}
