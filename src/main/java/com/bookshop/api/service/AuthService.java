package com.bookshop.api.service;

import com.bookshop.api.dto.LoginRequest;
import com.bookshop.api.dto.LoginResponse;
import com.bookshop.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        return new LoginResponse(jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()));
    }
}
