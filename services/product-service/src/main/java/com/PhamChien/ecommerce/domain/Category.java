package com.PhamChien.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends AbstractEntity<Long>{
    @Column(name = "name")
    private String name;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @Builder.Default
    private Set<ProductCategory> products = new HashSet<>();
}
