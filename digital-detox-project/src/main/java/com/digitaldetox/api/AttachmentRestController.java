package com.digitaldetox.api;

import com.digitaldetox.core.exceptions.EntityNotFoundException;
import com.digitaldetox.core.exceptions.FileUploadException;
import com.digitaldetox.dto.attachment.AttachmentFileDTO;
import com.digitaldetox.dto.attachment.AttachmentReadOnlyDTO;
import com.digitaldetox.service.IAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/check-ins/{checkInUuid}/attachments")
@RequiredArgsConstructor
public class AttachmentRestController {

    private final IAttachmentService attachmentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentReadOnlyDTO> upload(
            @PathVariable UUID checkInUuid,
            @RequestParam("file") MultipartFile file)
            throws EntityNotFoundException, FileUploadException {

        AttachmentReadOnlyDTO created = attachmentService.uploadCheckInAttachment(checkInUuid, file);
        return ResponseEntity
                .created(URI.create("/api/v1/check-ins/" + checkInUuid + "/attachments/" + created.uuid()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentReadOnlyDTO>> list(@PathVariable UUID checkInUuid)
            throws EntityNotFoundException {
        return ResponseEntity.ok(attachmentService.getCheckInAttachments(checkInUuid));
    }

    @GetMapping("/{attachmentUuid}")
    public ResponseEntity<org.springframework.core.io.Resource> download(
            @PathVariable UUID checkInUuid,
            @PathVariable UUID attachmentUuid)
            throws EntityNotFoundException, FileUploadException {

        AttachmentFileDTO file = attachmentService.downloadCheckInAttachment(checkInUuid, attachmentUuid);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.originalFilename() + "\"")
                .body(file.resource());
    }
}
