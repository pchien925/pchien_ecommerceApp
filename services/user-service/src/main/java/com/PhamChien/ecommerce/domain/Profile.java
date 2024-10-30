package com.PhamChien.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile")
public class Profile extends AbstractEntity<Long>{
    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "street")
    private String street;

    @Column(name = "street_number")
    private String street_number;

    @Column(name = "full_address")
    private String fullAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
