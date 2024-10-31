package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Profile;
import com.PhamChien.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByUser(User user);
}
