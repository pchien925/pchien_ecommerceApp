package com.PhamChien.ecommerce.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    String Id;

    String fullAddress;

    String district;

    String province;

    String city;

    String userId;
}
