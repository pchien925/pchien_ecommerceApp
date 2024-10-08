package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.UserProfile;
import com.PhamChien.ecommerce.dto.request.UserProfileRequest;
import com.PhamChien.ecommerce.dto.response.UserProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest userProfileRequest);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
