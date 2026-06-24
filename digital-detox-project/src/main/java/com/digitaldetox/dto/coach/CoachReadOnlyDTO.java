package com.digitaldetox.dto.coach;

public record CoachReadOnlyDTO(
        String uuid,
        String displayName,
        String specialty,
        String bio,
        boolean approved,
        Integer yearsExperience,
        String username,
        String email
) {
}
