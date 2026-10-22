package com.pm.backend.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMessageRequest(
        @NotBlank(message = "Message content must not be blank")
        @Size(max = 100, message = "Message must be 100 characters or fewer")
        String content
) {
}
