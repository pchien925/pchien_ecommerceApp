package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
}
