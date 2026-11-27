package at.htl.workloads.device;

import at.htl.model.device.DeviceDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceMapper {

    public DeviceDTO toDto(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getName(),
                device.getType().name(),
                device.getUser().getId()
        );
    }
}
