package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.dto.goal.GoalInsertDTO;
import com.digitaldetox.dto.goal.GoalReadOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface IGoalService {

    GoalReadOnlyDTO addGoal(UUID planUuid, GoalInsertDTO dto) throws EntityNotFoundException;

    List<GoalReadOnlyDTO> getGoalsByPlan(UUID planUuid) throws EntityNotFoundException;
}
