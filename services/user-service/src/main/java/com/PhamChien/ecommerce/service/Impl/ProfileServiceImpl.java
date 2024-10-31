package com.PhamChien.ecommerce.service.Impl;


import com.PhamChien.ecommerce.domain.Profile;
import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.ProfileRequest;
import com.PhamChien.ecommerce.dto.response.ProfileResponse;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.ProfileMapper;
import com.PhamChien.ecommerce.repository.ProfileRepository;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse createProfile(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Profile profile = profileMapper.toProfile(request);
        profile.setUser(user);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @Override
    public ProfileResponse updateProfile(Long profileId, ProfileRequest request){
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile Not Found"));
        profileMapper.updateProfile(profile, request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @Override
    public String deleteProfile(Long profileId){
        profileRepository.deleteById(profileId);
        return "deleted";
    }

    @Override
    public ProfileResponse getProfile(Long profileId){
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ResourceNotFoundException("Profile Not Found"));
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public List<ProfileResponse> getAllProfiles(){
        return profileRepository.findAll().stream().map(profileMapper::toProfileResponse).toList();
    }

    @Override
    public List<ProfileResponse> getUserProfiles(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return profileRepository.findAllByUser(user).stream().map(profileMapper::toProfileResponse).toList();
    }
}
