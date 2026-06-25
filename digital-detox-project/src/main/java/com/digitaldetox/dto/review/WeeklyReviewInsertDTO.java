package com.digitaldetox.dto.review;

import com.digitaldetox.model.enums.RiskLevel;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WeeklyReviewInsertDTO(
        @NotNull LocalDate weekStart,
        String summary,
        String recommendation,
        RiskLevel riskLevel
) {
}
