package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.AuthenticationRequest;
import com.PhamChien.ecommerce.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
