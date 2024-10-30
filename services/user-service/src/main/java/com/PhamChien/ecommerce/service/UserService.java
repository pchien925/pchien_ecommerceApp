package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreationRequest request);
}
