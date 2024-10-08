package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.UserRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest);

    String deleteUser(String userId);

    UserResponse getUserById(String id);

    List<UserResponse> getAllUsers();
}
