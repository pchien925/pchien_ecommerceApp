package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Address;
import com.PhamChien.ecommerce.dto.request.AddressRequest;
import com.PhamChien.ecommerce.dto.response.AddressResponse;
import com.PhamChien.ecommerce.exception.AppException;
import com.PhamChien.ecommerce.exception.ErrorCode;
import com.PhamChien.ecommerce.mapper.AddressMapper;
import com.PhamChien.ecommerce.mapper.UserMapper;
import com.PhamChien.ecommerce.repository.AddressRepository;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final UserMapper userMapper;

    AddressRepository addressRepository;
    AddressMapper addressMapper;

    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
        addressRepository.findByCityAndDistrictAndProvinceAndFullAddressAndUserId(addressRequest.getCity(), addressRequest.getDistrict(), addressRequest.getProvince(), addressRequest.getFullAddress(), addressRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_EXISTED));
        return addressMapper.toAddressResponse(addressRepository.save(addressMapper.toAddress(addressRequest)));
    }

    @Override
    public AddressResponse updateAddress(String addressId, AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        addressMapper.updateAddress(address, addressRequest);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    @Override
    public String deleteAddress(String id) {
        try {
            addressRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.ADDRESS_NOT_EXISTED);
        }
        return "deleted successfully";
    }

    @Override
    public AddressResponse getAddress(String id) {
        return addressMapper.toAddressResponse(addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED)));
    }

    @Override
    public List<AddressResponse> getAddresses() {
        return addressRepository.findAll().stream().map(addressMapper::toAddressResponse).toList();
    }
}
