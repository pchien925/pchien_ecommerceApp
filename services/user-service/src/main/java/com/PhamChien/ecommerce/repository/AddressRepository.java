package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Address;
import com.PhamChien.ecommerce.dto.request.AddressRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository <Address, String>{
    Optional<Address> findByCityAndDistrictAndProvinceAndFullAddressAndUserId(String city, String district, String province, String fullAddress, String userId);
}
