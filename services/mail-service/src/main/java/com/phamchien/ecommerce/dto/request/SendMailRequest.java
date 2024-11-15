package com.PhamChien.ecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMailRequest {
    private String recipient;
    private String msgBody;
    private String subject;
}
