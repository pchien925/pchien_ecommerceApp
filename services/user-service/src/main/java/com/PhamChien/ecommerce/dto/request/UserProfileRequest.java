package com.PhamChien.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {

    String userId;

    String firstName;

    String lastName;

    @Size(max = 20, min = 9)
    String phoneNumber;

    LocalDate dateOfBirth;

    @Size(max = 20)
    String gender;

    String profilePictureUrl;
}
