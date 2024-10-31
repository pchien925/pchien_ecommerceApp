package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class ProfileRequest {
    private String name;

    private String phone;

    private String province;

    private String district;

    private String street;

    private String street_number;

    private String fullAddress;
}
