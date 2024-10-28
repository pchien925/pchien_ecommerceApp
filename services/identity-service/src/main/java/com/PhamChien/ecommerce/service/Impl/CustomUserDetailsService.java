package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public UserCredential loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
