package com.PhamChien.ecommerce.service.impl;

import com.PhamChien.ecommerce.dto.request.SendMailRequest;
import com.PhamChien.ecommerce.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @KafkaListener(topics = "send-email-register-topic", groupId = "send-email-register-group")
    public void sendConfirmRegisterLink(String message){
        log.info(message);
        SimpleMailMessage mail = new SimpleMailMessage();
        String[] arr = message.split(",");
        String emailTo = arr[0].substring(arr[0].indexOf('=') + 1);
        String username = arr[1].substring(arr[1].indexOf('=') + 1);
        String verifyCode = arr[2].substring(arr[2].indexOf('=') + 1);
        try {
            mail.setTo(emailTo);
            mail.setSubject(username);
            mail.setText("http://localhost:8082/api/v1/auth/activate-account?code=" + verifyCode);
            mail.setFrom(fromEmail);

            mailSender.send(mail);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    @KafkaListener(topics = "send-email-forgot-password-topic", groupId = "send-email-forgot-password-group")
    public void sendForgotPasswordLink(String message){
        log.info(message);
        SimpleMailMessage mail = new SimpleMailMessage();
        String[] arr = message.split(",");
        String emailTo = arr[0].substring(arr[0].indexOf('=') + 1);
        String body = arr[1].substring(arr[1].indexOf('=') + 1);
        try {
            mail.setTo(emailTo);
            mail.setSubject("forgot-password");
            mail.setText(body);
            mail.setFrom(fromEmail);

            mailSender.send(mail);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

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

    @KafkaListener(topics = "order-mail-topic", groupId = "order-mail-group")
    public void sendOrderMail(String message){
        SimpleMailMessage mail = new SimpleMailMessage();
        //thêm logic lấy thông tin từ message
        try {
            mail.setTo("pcchien250904@gmail.com");
            mail.setSubject("Order");
            mail.setText("Bạn đã tạo đơn hàng thành công.");
            mail.setFrom(fromEmail);

            mailSender.send(mail);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
