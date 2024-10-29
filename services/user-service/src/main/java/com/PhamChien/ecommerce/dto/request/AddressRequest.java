package com.PhamChien.ecommerce.dto.request;

import com.PhamChien.ecommerce.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    @NotEmpty
    String fullAddress;

    @NotEmpty
    String district;

    @NotEmpty
    String province;

    @NotEmpty
    String city;

    @NotNull
    String userId;
}
