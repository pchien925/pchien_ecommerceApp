package com.PhamChien.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "access_token", columnDefinition = "varchar(500)")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "varchar(500)")
    private String refreshToken;
}
