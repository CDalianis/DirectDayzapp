package com.digitaldetox.dto.coach;

import jakarta.validation.constraints.NotBlank;

public record CoachUpdateDTO(
        @NotBlank String displayName,
        String specialty,
        String bio,
        Integer yearsExperience
) {
}
