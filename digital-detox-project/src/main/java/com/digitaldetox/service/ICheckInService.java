package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityAlreadyExistsException;
import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.filters.CheckInFilters;
import com.digitaldetox.dto.checkin.CheckInInsertDTO;
import com.digitaldetox.dto.checkin.CheckInReadOnlyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICheckInService {

    CheckInReadOnlyDTO createCheckIn(UUID planUuid, CheckInInsertDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException;

    List<CheckInReadOnlyDTO> getCheckInsByPlan(UUID planUuid) throws EntityNotFoundException;

    Page<CheckInReadOnlyDTO> getCheckInsPaginated(UUID planUuid, CheckInFilters filters, Pageable pageable)
            throws EntityNotFoundException;
}
