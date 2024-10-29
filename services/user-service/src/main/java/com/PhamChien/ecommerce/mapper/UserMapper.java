package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.UserRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
    void updateUser(UserUpdateRequest userUpdateRequest, @MappingTarget User user);
}
