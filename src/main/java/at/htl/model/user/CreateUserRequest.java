package at.htl.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(max = 50, message = "Username must be at most 50 characters")
        String username,

        @Email(message = "Email must be valid")
        @Size(max = 200, message = "Email must be at most 200 characters")
        String email
) {
}
