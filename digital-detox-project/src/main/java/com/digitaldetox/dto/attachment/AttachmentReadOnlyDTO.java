package com.digitaldetox.dto.attachment;

public record AttachmentReadOnlyDTO(
        String uuid,
        String originalFilename,
        String contentType,
        String fileExtension,
        Long sizeBytes
) {
}
