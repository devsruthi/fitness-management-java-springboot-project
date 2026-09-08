package com.fitness.management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record MemberRegistrationRequest(
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Email @Size(max = 150) String emailId,
        @NotBlank @Size(min = 6, max = 50) String password,
        @Size(max = 20) String phoneNo,
        @NotNull @Past LocalDate dateOfBirth
) {
}
