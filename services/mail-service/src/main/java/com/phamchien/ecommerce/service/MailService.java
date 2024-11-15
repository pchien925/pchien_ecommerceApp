package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.SendMailRequest;

public interface MailService {
    String sendSimpleMail(SendMailRequest sendMailRequest);
}
