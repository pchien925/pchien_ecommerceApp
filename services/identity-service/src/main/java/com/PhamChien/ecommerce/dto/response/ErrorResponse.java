package com.PhamChien.ecommerce.dto.response;

public class ErrorResponse extends ApiResponse{
    public ErrorResponse(int status, String message) {
        super(status, message);
    }
}
