package com.PhamChien.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brand")
public class Brand extends AbstractEntity<Integer>{
    private String name;
    private String description;
    private String slug;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    @Builder.Default
    private Set<Product> products = new HashSet<Product>();
}
