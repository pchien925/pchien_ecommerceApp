package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.ProfileRequest;
import com.PhamChien.ecommerce.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    ProfileResponse createProfile(Long userId, ProfileRequest request);

    ProfileResponse updateProfile(Long id, ProfileRequest request);

    String deleteProfile(Long id);

    ProfileResponse getProfile(Long id);

    List<ProfileResponse> getAllProfiles();

    List<ProfileResponse> getUserProfiles(Long userId);
}
