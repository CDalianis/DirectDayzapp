package com.digitaldetox.dto.checkin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CheckInInsertDTO(
        @NotNull LocalDate entryDate,
        @NotNull @Min(0) Integer totalScreenMinutes,
        @Min(0) Integer socialMediaMinutes,
        BigDecimal sleepHours,
        @Min(1) @Max(10) Integer focusScore,
        @Min(1) @Max(10) Integer stressLevel,
        @Min(1) @Max(10) Integer cravingLevel,
        String notes
) {
}
