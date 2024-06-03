package com.ph.security.service;

import com.ph.domain.entities.User;
import com.ph.payload.mapper.UserMapper;
import com.ph.payload.request.LoginRequest;
import com.ph.payload.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user login request.
     *
     * @param request the login request containing the user's email and password
     * @return the login response containing the user information and JWT token
     */
    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        // Authenticate the user
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // Get the user details
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        User user = (User) userDetails;
        // Generate JWT token
        var jwtToken = tokenService.generateToken(user);
        // Map user and JWT token to login response
        return userMapper.toLoginResponse(user, jwtToken);
    }

}
