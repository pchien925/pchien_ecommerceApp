package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.dto.request.SendMailRequest;
import com.PhamChien.ecommerce.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public String sendSimpleMail(SendMailRequest sendMailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(sendMailRequest.getRecipient());
            message.setSubject(sendMailRequest.getSubject());
            message.setText(sendMailRequest.getMsgBody());
            message.setFrom(fromEmail);

            mailSender.send(message);
            return "send mail success";
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
