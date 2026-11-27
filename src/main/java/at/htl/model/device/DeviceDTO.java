package at.htl.model.device;

public record DeviceDTO(
        Long id,
        String name,
        String type,
        Long userId
) {
}
