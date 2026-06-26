package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityInvalidArgumentException;
import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.filters.DetoxPlanFilters;
import com.digitaldetox.dto.plan.DetoxPlanInsertDTO;
import com.digitaldetox.dto.plan.DetoxPlanReadOnlyDTO;
import com.digitaldetox.dto.plan.DetoxPlanUpdateDTO;
import com.digitaldetox.model.enums.DetoxPlanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IDetoxPlanService {

    DetoxPlanReadOnlyDTO createPlan(String coachUsername, DetoxPlanInsertDTO dto)
            throws EntityNotFoundException, EntityInvalidArgumentException;

    DetoxPlanReadOnlyDTO getByUuid(UUID uuid) throws EntityNotFoundException;

    List<DetoxPlanReadOnlyDTO> getPlansForCurrentUser(String username);

    Page<DetoxPlanReadOnlyDTO> getPlansPaginated(String username, DetoxPlanFilters filters, Pageable pageable);

    DetoxPlanReadOnlyDTO updatePlan(DetoxPlanUpdateDTO dto) throws EntityNotFoundException;

    DetoxPlanReadOnlyDTO updateStatus(UUID uuid, DetoxPlanStatus status) throws EntityNotFoundException;
}
