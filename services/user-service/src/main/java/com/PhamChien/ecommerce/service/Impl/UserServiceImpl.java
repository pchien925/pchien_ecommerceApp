package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import com.PhamChien.ecommerce.mapper.UserMapper;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreationRequest request){
       return null;
    }
}
