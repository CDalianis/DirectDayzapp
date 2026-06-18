package com.digitaldetox.repository;

import com.digitaldetox.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findByUuid(UUID uuid);

    Optional<Attachment> findByUuidAndDeletedFalse(UUID uuid);

    Optional<Attachment> findBySavedName(String savedName);

    List<Attachment> findAllByDailyCheckInUuidAndDeletedFalse(UUID dailyCheckInUuid);

    Optional<Attachment> findByUuidAndDeletedFalseAndDailyCheckIn_Uuid(UUID uuid, UUID dailyCheckInUuid);
}
