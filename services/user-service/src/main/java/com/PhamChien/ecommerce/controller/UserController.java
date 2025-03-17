package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.UserCreationRequest;
import com.PhamChien.ecommerce.dto.request.UserUpdateRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.UserResponse;
import com.PhamChien.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(userService.create(request))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> update(@PathVariable("userId") Long id, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .data(userService.update(id,request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> delete(@PathVariable("userId") Long id){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(userService.delete(id))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> get(@PathVariable("userId") long userId){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.FOUND.value())
                .data(userService.getUser(userId))
                .build();
    }

    @GetMapping("/credential/{credentialId}")
    ApiResponse<UserResponse> getByCredentialId(@PathVariable("credentialId")String credentialId){
        return ApiResponse.<UserResponse>builder()
                .status(HttpStatus.FOUND.value())
                .data(userService.getUserByCredentialId(credentialId))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAll(){
        return ApiResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(userService.getAllUser())
                .build();
    }

    @GetMapping("/paging")
    ApiResponse<PageResponse<UserResponse>> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   @RequestParam(value = "fullName", required = false) String fullName,
                                                   @RequestParam(value = "credentialId", required = false) String credentialId,
                                                   @RequestParam(value = "field", required = false, defaultValue = "id") String sortField,
                                                   @RequestParam(value = "order", defaultValue = "ASC") String sortOrder
             ){
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(userService.getAll(fullName, phone, credentialId, page, size, sortField, sortOrder))
                .build();
    }
}
