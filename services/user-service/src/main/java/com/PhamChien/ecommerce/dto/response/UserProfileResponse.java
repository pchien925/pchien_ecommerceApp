package com.PhamChien.ecommerce.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;

    String firstName;

    String lastName;

    String phoneNumber;

    LocalDate dateOfBirth;

    String gender;

    String profilePictureUrl;

    String userId;
}
