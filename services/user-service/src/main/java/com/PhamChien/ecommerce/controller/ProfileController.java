package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.ProfileRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.ProfileResponse;
import com.PhamChien.ecommerce.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{profileId}")
    public ApiResponse<ProfileResponse> getProfile(@PathVariable("profileId") Long profileId) {
        return ApiResponse.<ProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .data(profileService.getProfile(profileId))
                .build();
    }

    @PostMapping("/{userId}")
    public ApiResponse<ProfileResponse> addProfile(@PathVariable("userId") Long userId, @RequestBody ProfileRequest profileRequest) {
        return ApiResponse.<ProfileResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(profileService.createProfile(userId, profileRequest))
                .build();
    }

    @PutMapping("/{profileId}")
    public ApiResponse<ProfileResponse> updateProfile(@PathVariable("profileId") Long profileId, @RequestBody ProfileRequest profileRequest) {
        return ApiResponse.<ProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .data(profileService.updateProfile(profileId, profileRequest))
                .build();
    }

    @DeleteMapping("/{profileId}")
    public ApiResponse<String> deleteProfile(@PathVariable("profileId") Long profileId) {
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(profileService.deleteProfile(profileId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProfileResponse>> getProfiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(profileService.getAllProfiles())
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ProfileResponse>> getUserProfiles(@PathVariable("userId") Long userId) {
        return ApiResponse.<List<ProfileResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(profileService.getUserProfiles(userId))
                .build();
    }
}
