package com.PhamChien.ecommerce.dto.request;

import com.PhamChien.ecommerce.util.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class UserCreationRequest {
    private String firstname;

    private String lastname;

    private Gender gender;

    private String phone;

    private LocalDate dob;

    private String credentialId;
}
