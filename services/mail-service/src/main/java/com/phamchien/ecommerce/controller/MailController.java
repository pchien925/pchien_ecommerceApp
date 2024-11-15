package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.SendMailRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Slf4j
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    ApiResponse<String> sendSimpleMail(@RequestBody SendMailRequest request){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(mailService.sendSimpleMail(request))
                .build();
    }
}
