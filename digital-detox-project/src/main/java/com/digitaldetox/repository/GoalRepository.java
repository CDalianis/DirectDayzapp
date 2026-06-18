package com.digitaldetox.repository;

import com.digitaldetox.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    Optional<Goal> findByUuid(UUID uuid);

    Optional<Goal> findByUuidAndDeletedFalse(UUID uuid);

    List<Goal> findAllByDetoxPlanUuidAndDeletedFalse(UUID detoxPlanUuid);
}
