package com.PhamChien.ecommerce.domain;

import com.PhamChien.ecommerce.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    String gender;

    @Column(name = "profile_picture_url")
    String profilePictureUrl;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;
}

