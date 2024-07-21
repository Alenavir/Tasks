package com.example.task.service.impl;

import com.example.task.service.AuthService;
import com.example.task.web.dto.auth.JwtRequest;
import com.example.task.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }

}
