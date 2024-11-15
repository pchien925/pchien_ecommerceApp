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
@Table(name = "product")
public class Product extends AbstractEntity<Long>{
    private String name;

    private String description;

    private String slug;

    private Double price;

    @Column(name = "promotional_price")
    private Double promotionalPrice;

    private String size;

    private String color;

    private Integer sold;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = false;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "parent_product_id")
    @JsonIgnore
    private Product parentProduct;
    
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Builder.Default
    private Set<ProductCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Builder.Default
    private Set<ProductMedia> medias = new HashSet<>();
}
