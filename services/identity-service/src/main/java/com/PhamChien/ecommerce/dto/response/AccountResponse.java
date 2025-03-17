package com.PhamChien.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private String id;

    private String username;

    private String email;

    private Boolean isActive;

    private LocalDateTime createdAt;    

    private LocalDateTime updatedAt;

    private List<String> role = new ArrayList<>();

}
