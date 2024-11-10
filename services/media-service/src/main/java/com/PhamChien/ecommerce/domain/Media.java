package com.PhamChien.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_key")
    private String mediaKey;

    private String caption;

    private String type;

    private String url;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
}
