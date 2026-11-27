package at.htl.model.listeningSession;

import java.time.LocalDateTime;

public record ListeningSessionDTO(
        Long id,
        Long userId,
        Long deviceId,
        String deviceName,
        Long audioItemId,
        String title,
        LocalDateTime startedAt,
        Integer listenedSeconds
) {
}
