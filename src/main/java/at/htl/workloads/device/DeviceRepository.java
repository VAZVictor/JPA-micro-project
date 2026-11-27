package at.htl.workloads.device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

    Optional<Device> findById(Long id);

    List<Device> findByUser(Long userId);

    void add(Device device);
}
