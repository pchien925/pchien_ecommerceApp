package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Profile;
import com.PhamChien.ecommerce.dto.request.ProfileRequest;
import com.PhamChien.ecommerce.dto.response.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileRequest request);
    ProfileResponse toProfileResponse(Profile profile);

    void updateProfile(@MappingTarget Profile profile, ProfileRequest request);
}
