package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.Profile;
import com.PhamChien.ecommerce.util.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;

    private String firstname;

    private String lastname;

    private Gender gender;

    private String phone;

    private LocalDate dob;

    private String credentialId;

    private Date createdAt;

    private Date updatedAt;

    private Set<Profile> profiles;
}
