package com.PhamChien.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int status;

    private String message;
    private T data;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
