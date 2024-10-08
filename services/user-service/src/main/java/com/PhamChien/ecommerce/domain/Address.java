package com.PhamChien.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "full_address")
    String fullAddress;

    @Column(name = "district")
    String district;

    @Column(name = "province")
    String province;

    @Column(name = "city")
    String city;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
