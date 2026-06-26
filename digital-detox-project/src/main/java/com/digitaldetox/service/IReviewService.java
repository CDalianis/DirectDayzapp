package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.dto.review.WeeklyReviewInsertDTO;
import com.digitaldetox.dto.review.WeeklyReviewReadOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface IReviewService {

    WeeklyReviewReadOnlyDTO createReview(String coachUsername, UUID planUuid, WeeklyReviewInsertDTO dto)
            throws EntityNotFoundException;

    List<WeeklyReviewReadOnlyDTO> getReviewsByPlan(UUID planUuid) throws EntityNotFoundException;
}
