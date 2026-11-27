package at.htl.model.listeningSession;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record CreateListeningSessionRequest(
        @NotNull(message = "User is required")
        Long userId,

        @NotNull(message = "Device is required")
        Long deviceId,

        @NotNull(message = "Audio item is required")
        Long audioItemId,

        @NotNull(message = "Start time is required")
        @PastOrPresent(message = "Start time must not be in the future")
        LocalDateTime startedAt,

        @NotNull(message = "Listened seconds are required")
        @PositiveOrZero(message = "Listened seconds must not be negative")
        Integer listenedSeconds
) {
}
