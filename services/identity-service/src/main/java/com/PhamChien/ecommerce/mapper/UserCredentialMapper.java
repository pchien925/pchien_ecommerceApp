package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.dto.request.RegisterRequest;
import com.PhamChien.ecommerce.dto.response.UserCredentialResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialMapper {
    UserCredential toUserCredential(RegisterRequest request);
    UserCredentialResponse toUserCredentialResponse(UserCredential userCredential);

}
