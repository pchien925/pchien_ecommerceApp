package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Address;
import com.PhamChien.ecommerce.dto.request.AddressRequest;
import com.PhamChien.ecommerce.dto.response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressRequest);
    AddressResponse toAddressResponse(Address address);

    @Mapping(target = "user", ignore = true)
    void updateAddress(@MappingTarget Address address, AddressRequest addressRequest);
}
