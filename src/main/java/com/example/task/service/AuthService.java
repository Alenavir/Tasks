package com.example.task.service;

import com.example.task.web.dto.auth.JwtRequest;
import com.example.task.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
