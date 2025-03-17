package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreationRequest request);

    UserResponse update(Long id, UserUpdateRequest request);


    String delete(Long id);

    UserResponse getUser(Long id);

    List<UserResponse> getAllUser();

    UserResponse getUserByCredentialId(String id);

    PageResponse<UserResponse> getAll(String fullName, String phone,String credentialId, int page, int size, String sortField, String sortOrder);
}
