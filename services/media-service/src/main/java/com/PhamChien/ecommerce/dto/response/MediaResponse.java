package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class MediaResponse {
    private Long id;

    private String mediaKey;


    private String caption;

    private String type;

    private String url;

    private Date createdAt;
}
