package com.digitaldetox.dto.plan;

import com.digitaldetox.model.enums.DetoxPlanStatus;

import java.time.LocalDate;

public record DetoxPlanReadOnlyDTO(
        String uuid,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        DetoxPlanStatus status,
        Integer targetScreenMinutes,
        Integer targetSocialMinutes,
        String focusArea,
        String memberProfileUuid,
        String memberDisplayName,
        String coachProfileUuid,
        String coachDisplayName
) {
}
