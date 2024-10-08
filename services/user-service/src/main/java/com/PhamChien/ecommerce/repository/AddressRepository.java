package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository <Address, String>{
}
