package at.htl.model.user;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {
}
