package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {
    private Long id;

    private String name;

    private String phone;

    private String province;

    private String district;

    private String street;

    private String street_number;

    private String fullAddress;

}
