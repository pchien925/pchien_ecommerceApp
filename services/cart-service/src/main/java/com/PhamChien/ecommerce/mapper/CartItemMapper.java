package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.CartItem;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "slug", target = "productSlug")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "promotionalPrice", target = "productPromotionalPrice")
    @Mapping(source = "size", target = "productSize")
    @Mapping(source = "color", target = "productColor")
    @Mapping(source = "sold", target = "productSold")
    @Mapping(source = "thumbnailUrl", target = "productThumbnailUrl")
    CartItem toCartItem(ProductResponse productResponse);

}
