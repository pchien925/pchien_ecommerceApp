package com.PhamChien.ecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class MediaRequestDTO {
    private String mediaKey;

    private String caption;

    private MultipartFile file;
}
