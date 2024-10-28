package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.SendMailRequest;

public interface EmailService {
    String sendSimpleMail(SendMailRequest sendMailRequest);
}
