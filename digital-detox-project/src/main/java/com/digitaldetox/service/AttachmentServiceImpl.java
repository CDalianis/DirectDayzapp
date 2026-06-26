package com.digitaldetox.service;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.exceptions.FileUploadException;
import com.digitaldetox.dto.attachment.AttachmentFileDTO;
import com.digitaldetox.dto.attachment.AttachmentReadOnlyDTO;
import com.digitaldetox.mapper.Mapper;
import com.digitaldetox.model.Attachment;
import com.digitaldetox.model.DailyCheckIn;
import com.digitaldetox.repository.AttachmentRepository;
import com.digitaldetox.repository.DailyCheckInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements IAttachmentService {

    private final DailyCheckInRepository dailyCheckInRepository;
    private final AttachmentRepository attachmentRepository;
    private final Mapper mapper;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Override
    @PreAuthorize("hasAuthority('UPLOAD_ATTACHMENT') and @securityService.isOwnCheckInMember(#checkInUuid, authentication)")
    @Transactional(rollbackFor = {EntityNotFoundException.class, FileUploadException.class})
    public AttachmentReadOnlyDTO uploadCheckInAttachment(UUID checkInUuid, MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        DailyCheckIn checkIn = dailyCheckInRepository.findByUuidAndDeletedFalse(checkInUuid)
                .orElseThrow(() -> new EntityNotFoundException("CheckIn", "Check-in with uuid=" + checkInUuid + " not found"));

        try {
            String originalFilename = file.getOriginalFilename() == null ? "upload" : file.getOriginalFilename();
            String savedName = UUID.randomUUID() + getFileExtension(originalFilename);
            Path filePath = Paths.get(uploadDir, savedName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);

            Tika tika = new Tika();
            String contentType = tika.detect(file.getInputStream());

            Attachment attachment = new Attachment();
            attachment.setOriginalFilename(originalFilename);
            attachment.setSavedName(savedName);
            attachment.setFilePath(filePath.toString());
            attachment.setContentType(contentType);
            attachment.setFileExtension(getFileExtension(originalFilename));
            attachment.setSizeBytes(file.getSize());

            checkIn.addAttachment(attachment);
            attachmentRepository.save(attachment);

            log.info("Attachment uploaded for check-in={}", checkInUuid);
            return mapper.mapToAttachmentReadOnlyDTO(attachment);
        } catch (IOException e) {
            throw new FileUploadException("Attachment", "Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_CHECKINS') or hasAuthority('VIEW_OWN_CHECKINS')")
    @Transactional(readOnly = true)
    public List<AttachmentReadOnlyDTO> getCheckInAttachments(UUID checkInUuid) throws EntityNotFoundException {
        if (!dailyCheckInRepository.findByUuidAndDeletedFalse(checkInUuid).isPresent()) {
            throw new EntityNotFoundException("CheckIn", "Check-in with uuid=" + checkInUuid + " not found");
        }
        return attachmentRepository.findAllByDailyCheckInUuidAndDeletedFalse(checkInUuid)
                .stream()
                .map(mapper::mapToAttachmentReadOnlyDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_CHECKINS') or hasAuthority('VIEW_OWN_CHECKINS')")
    @Transactional(readOnly = true)
    public AttachmentFileDTO downloadCheckInAttachment(UUID checkInUuid, UUID attachmentUuid)
            throws EntityNotFoundException, FileUploadException {

        if (!dailyCheckInRepository.findByUuidAndDeletedFalse(checkInUuid).isPresent()) {
            throw new EntityNotFoundException("CheckIn", "Check-in with uuid=" + checkInUuid + " not found");
        }

        Attachment attachment = attachmentRepository
                .findByUuidAndDeletedFalseAndDailyCheckIn_Uuid(attachmentUuid, checkInUuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Attachment", "Attachment with uuid=" + attachmentUuid + " not found"));

        Path path = Paths.get(attachment.getFilePath());
        if (!Files.exists(path)) {
            throw new FileUploadException("Attachment", "Stored file is missing on disk");
        }

        Resource resource = new FileSystemResource(path);
        return new AttachmentFileDTO(resource, attachment.getContentType(), attachment.getOriginalFilename());
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
