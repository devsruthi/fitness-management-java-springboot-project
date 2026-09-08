package com.fitness.management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank @Email String emailId,
        @NotBlank String password
) {
}
