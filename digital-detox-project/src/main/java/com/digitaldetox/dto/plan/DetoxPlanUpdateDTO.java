package com.digitaldetox.dto.plan;

import com.digitaldetox.model.enums.DetoxPlanStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record DetoxPlanUpdateDTO(
        @NotNull UUID uuid,
        @NotBlank String title,
        String description,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull DetoxPlanStatus status,
        Integer targetScreenMinutes,
        Integer targetSocialMinutes,
        String focusArea
) {
}
