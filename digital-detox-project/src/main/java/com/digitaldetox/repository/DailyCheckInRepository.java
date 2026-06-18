package com.digitaldetox.repository;

import com.digitaldetox.model.DailyCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyCheckInRepository extends JpaRepository<DailyCheckIn, Long>,
        JpaSpecificationExecutor<DailyCheckIn> {

    Optional<DailyCheckIn> findByUuid(UUID uuid);

    Optional<DailyCheckIn> findByUuidAndDeletedFalse(UUID uuid);

    Optional<DailyCheckIn> findByDetoxPlanUuidAndEntryDateAndDeletedFalse(UUID detoxPlanUuid, LocalDate entryDate);

    List<DailyCheckIn> findAllByDetoxPlanUuidAndDeletedFalseOrderByEntryDateDesc(UUID detoxPlanUuid);

    boolean existsByDetoxPlanUuidAndEntryDateAndDeletedFalse(UUID detoxPlanUuid, LocalDate entryDate);
}
