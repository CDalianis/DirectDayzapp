package com.digitaldetox.repository;

import com.digitaldetox.model.DetoxPlan;
import com.digitaldetox.model.enums.DetoxPlanStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DetoxPlanRepository extends JpaRepository<DetoxPlan, Long>,
        JpaSpecificationExecutor<DetoxPlan> {

    Optional<DetoxPlan> findByUuid(UUID uuid);

    Optional<DetoxPlan> findByUuidAndDeletedFalse(UUID uuid);

    @EntityGraph(attributePaths = {"memberProfile", "coachProfile", "goals"})
    List<DetoxPlan> findAllByDeletedFalse();

    List<DetoxPlan> findAllByMemberProfileUuidAndDeletedFalse(UUID memberUuid);

    List<DetoxPlan> findAllByCoachProfileUuidAndDeletedFalse(UUID coachUuid);

    List<DetoxPlan> findAllByStatusAndDeletedFalse(DetoxPlanStatus status);
}
