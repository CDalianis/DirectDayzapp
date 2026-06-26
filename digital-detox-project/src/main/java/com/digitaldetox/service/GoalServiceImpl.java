package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.dto.goal.GoalInsertDTO;
import com.digitaldetox.dto.goal.GoalReadOnlyDTO;
import com.digitaldetox.mapper.Mapper;
import com.digitaldetox.model.DetoxPlan;
import com.digitaldetox.model.Goal;
import com.digitaldetox.repository.DetoxPlanRepository;
import com.digitaldetox.repository.GoalRepository;
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
public class GoalServiceImpl implements IGoalService {

    private final GoalRepository goalRepository;
    private final DetoxPlanRepository detoxPlanRepository;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('EDIT_PLAN') and @securityService.isAssignedCoachForPlan(#planUuid, authentication)")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public GoalReadOnlyDTO addGoal(UUID planUuid, GoalInsertDTO dto) throws EntityNotFoundException {
        DetoxPlan plan = detoxPlanRepository.findByUuidAndDeletedFalse(planUuid)
                .orElseThrow(() -> new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found"));

        Goal goal = mapper.mapToGoalEntity(dto);
        plan.addGoal(goal);
        goalRepository.save(goal);

        log.info("Goal added to plan={}", planUuid);
        return mapper.mapToGoalReadOnlyDTO(goal);
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_PLANS') or (hasAuthority('VIEW_OWN_PLANS') and @securityService.isPlanParticipant(#planUuid, authentication))")
    @Transactional(readOnly = true)
    public List<GoalReadOnlyDTO> getGoalsByPlan(UUID planUuid) throws EntityNotFoundException {
        if (!detoxPlanRepository.findByUuidAndDeletedFalse(planUuid).isPresent()) {
            throw new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found");
        }
        return goalRepository.findAllByDetoxPlanUuidAndDeletedFalse(planUuid)
                .stream()
                .map(mapper::mapToGoalReadOnlyDTO)
                .toList();
    }
}
