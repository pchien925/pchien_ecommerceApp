package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.AddressRequest;
import com.PhamChien.ecommerce.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressRequest addressRequest);
    AddressResponse updateAddress(String addressId, AddressRequest addressRequest);
    String deleteAddress(String id);
    AddressResponse getAddress(String id);
    List<AddressResponse> getAddresses();
}
