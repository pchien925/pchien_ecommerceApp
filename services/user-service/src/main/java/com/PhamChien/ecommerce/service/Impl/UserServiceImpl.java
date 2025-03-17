package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import com.PhamChien.ecommerce.mapper.UserMapper;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageResponse<UserResponse> getAll(String fullName, String phone, String credentialId, int page, int size , String sortField, String sortOrder){

        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> products = userRepository.searchUsers(fullName, phone, credentialId, pageable);
        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }

}
