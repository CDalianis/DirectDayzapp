package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.dto.review.WeeklyReviewInsertDTO;
import com.digitaldetox.dto.review.WeeklyReviewReadOnlyDTO;
import com.digitaldetox.mapper.Mapper;
import com.digitaldetox.model.CoachProfile;
import com.digitaldetox.model.DetoxPlan;
import com.digitaldetox.model.WeeklyReview;
import com.digitaldetox.repository.CoachProfileRepository;
import com.digitaldetox.repository.DetoxPlanRepository;
import com.digitaldetox.repository.WeeklyReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements IReviewService {

    private final WeeklyReviewRepository weeklyReviewRepository;
    private final DetoxPlanRepository detoxPlanRepository;
    private final CoachProfileRepository coachProfileRepository;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('CREATE_REVIEW') and @securityService.isAssignedCoachForPlan(#planUuid, authentication)")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public WeeklyReviewReadOnlyDTO createReview(String coachUsername, UUID planUuid, WeeklyReviewInsertDTO dto)
            throws EntityNotFoundException {

        CoachProfile coach = coachProfileRepository.findByUserUsernameAndDeletedFalse(coachUsername)
                .orElseThrow(() -> new EntityNotFoundException("Coach", "Coach profile not found"));

        DetoxPlan plan = detoxPlanRepository.findByUuidAndDeletedFalse(planUuid)
                .orElseThrow(() -> new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found"));

        WeeklyReview review = mapper.mapToWeeklyReviewEntity(dto);
        review.setCoachProfile(coach);
        plan.addWeeklyReview(review);
        weeklyReviewRepository.save(review);

        log.info("Weekly review created for plan={}", planUuid);
        return mapper.mapToWeeklyReviewReadOnlyDTO(review);
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_REVIEWS') or (hasAuthority('VIEW_OWN_PLANS') and @securityService.isPlanParticipant(#planUuid, authentication))")
    @Transactional(readOnly = true)
    public List<WeeklyReviewReadOnlyDTO> getReviewsByPlan(UUID planUuid) throws EntityNotFoundException {
        if (!detoxPlanRepository.findByUuidAndDeletedFalse(planUuid).isPresent()) {
            throw new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found");
        }
        return weeklyReviewRepository.findAllByDetoxPlanUuidAndDeletedFalseOrderByWeekStartDesc(planUuid)
                .stream()
                .map(mapper::mapToWeeklyReviewReadOnlyDTO)
                .toList();
    }
}
