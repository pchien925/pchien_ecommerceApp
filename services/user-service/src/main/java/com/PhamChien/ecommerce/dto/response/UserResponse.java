package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.Address;
import com.PhamChien.ecommerce.domain.UserProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    LocalDateTime createdAt;

    LocalDateTime updatedAt;


    @JsonProperty("userProfile")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    UserProfile userProfile;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    Set<Address> addresses;
}
