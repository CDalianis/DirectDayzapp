package com.digitaldetox.dto.attachment;

import org.springframework.core.io.Resource;

public record AttachmentFileDTO(
        Resource resource,
        String contentType,
        String originalFilename
) {
}
