package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import com.PhamChien.ecommerce.mapper.UserMapper;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreationRequest request){
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(request)));
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public String delete(Long id){
        userRepository.deleteById(id);
        return "deleted";
    }

    @Override
    public UserResponse getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getUserByCredentialId(String id){
        User user = userRepository.findByCredentialId(id).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return userMapper.toUserResponse(user);
    }
}
