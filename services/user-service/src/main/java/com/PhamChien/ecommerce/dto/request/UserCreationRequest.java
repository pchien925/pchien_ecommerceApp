package com.PhamChien.ecommerce.dto.request;

import com.PhamChien.ecommerce.util.Gender;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class UserCreationRequest {

    private String fullName;

    private String   avatarUrl;

    private Gender gender;

    private String phone;

    private LocalDate dob;

    private String credentialId;
}
