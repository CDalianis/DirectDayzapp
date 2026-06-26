package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityAlreadyExistsException;
import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.filters.CheckInFilters;
import com.digitaldetox.specification.CheckInSpecification;
import com.digitaldetox.dto.checkin.CheckInInsertDTO;
import com.digitaldetox.dto.checkin.CheckInReadOnlyDTO;
import com.digitaldetox.mapper.Mapper;
import com.digitaldetox.model.DailyCheckIn;
import com.digitaldetox.model.DetoxPlan;
import com.digitaldetox.repository.DailyCheckInRepository;
import com.digitaldetox.repository.DetoxPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckInServiceImpl implements ICheckInService {

    private final DailyCheckInRepository dailyCheckInRepository;
    private final DetoxPlanRepository detoxPlanRepository;
    private final Mapper mapper;

    @Override
    @PreAuthorize("hasAuthority('CREATE_CHECKIN') and @securityService.isOwnPlanMember(#planUuid, authentication)")
    @Transactional(rollbackFor = {EntityNotFoundException.class, EntityAlreadyExistsException.class})
    public CheckInReadOnlyDTO createCheckIn(UUID planUuid, CheckInInsertDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException {

        DetoxPlan plan = detoxPlanRepository.findByUuidAndDeletedFalse(planUuid)
                .orElseThrow(() -> new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found"));

        if (dailyCheckInRepository.existsByDetoxPlanUuidAndEntryDateAndDeletedFalse(planUuid, dto.entryDate())) {
            throw new EntityAlreadyExistsException("CheckIn", "Check-in already exists for date " + dto.entryDate());
        }

        DailyCheckIn checkIn = mapper.mapToCheckInEntity(dto);
        plan.addDailyCheckIn(checkIn);
        dailyCheckInRepository.save(checkIn);

        log.info("Check-in created for plan={} on date={}", planUuid, dto.entryDate());
        return mapper.mapToCheckInReadOnlyDTO(checkIn);
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_CHECKINS') or (hasAuthority('VIEW_OWN_CHECKINS') and @securityService.isPlanParticipant(#planUuid, authentication))")
    @Transactional(readOnly = true)
    public List<CheckInReadOnlyDTO> getCheckInsByPlan(UUID planUuid) throws EntityNotFoundException {
        if (!detoxPlanRepository.findByUuidAndDeletedFalse(planUuid).isPresent()) {
            throw new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found");
        }
        return dailyCheckInRepository.findAllByDetoxPlanUuidAndDeletedFalseOrderByEntryDateDesc(planUuid)
                .stream()
                .map(mapper::mapToCheckInReadOnlyDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_CHECKINS') or (hasAuthority('VIEW_OWN_CHECKINS') and @securityService.isPlanParticipant(#planUuid, authentication))")
    @Transactional(readOnly = true)
    public Page<CheckInReadOnlyDTO> getCheckInsPaginated(UUID planUuid, CheckInFilters filters, Pageable pageable)
            throws EntityNotFoundException {

        if (!detoxPlanRepository.findByUuidAndDeletedFalse(planUuid).isPresent()) {
            throw new EntityNotFoundException("DetoxPlan", "Plan with uuid=" + planUuid + " not found");
        }

        CheckInFilters effectiveFilters = filters == null ? CheckInFilters.builder().build() : filters;
        return dailyCheckInRepository.findAll(CheckInSpecification.build(planUuid, effectiveFilters), pageable)
                .map(mapper::mapToCheckInReadOnlyDTO);
    }
}
