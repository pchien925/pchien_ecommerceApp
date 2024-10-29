package com.PhamChien.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUpdateRequest {
    @NotEmpty
    String fullAddress;

    @NotEmpty
    String district;

    @NotEmpty
    String province;

    @NotEmpty
    String city;
}
