package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Order;
import com.PhamChien.ecommerce.dto.request.OrderRequest;
import com.PhamChien.ecommerce.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);
}
