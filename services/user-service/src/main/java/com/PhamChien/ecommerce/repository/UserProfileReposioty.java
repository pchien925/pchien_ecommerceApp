package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileReposioty extends JpaRepository<UserProfile, String> {
}
