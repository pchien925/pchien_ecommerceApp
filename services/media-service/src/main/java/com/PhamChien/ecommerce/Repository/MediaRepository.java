package com.PhamChien.ecommerce.Repository;

import com.PhamChien.ecommerce.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    boolean existsByMediaKey(String mediaKey);
}
