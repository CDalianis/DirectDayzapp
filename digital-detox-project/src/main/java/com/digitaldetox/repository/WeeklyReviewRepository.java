package com.digitaldetox.repository;

import com.digitaldetox.model.WeeklyReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeeklyReviewRepository extends JpaRepository<WeeklyReview, Long> {

    Optional<WeeklyReview> findByUuid(UUID uuid);

    Optional<WeeklyReview> findByUuidAndDeletedFalse(UUID uuid);

    List<WeeklyReview> findAllByDetoxPlanUuidAndDeletedFalseOrderByWeekStartDesc(UUID detoxPlanUuid);
}
