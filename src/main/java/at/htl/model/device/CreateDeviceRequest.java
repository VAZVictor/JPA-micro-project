package at.htl.model.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDeviceRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @NotBlank(message = "Type is required")
        @Pattern(regexp = "PHONE|DESKTOP|TABLET|SPEAKER|WATCH",
                message = "Type must be one of PHONE, DESKTOP, TABLET, SPEAKER, WATCH")
        String type
) {
}
