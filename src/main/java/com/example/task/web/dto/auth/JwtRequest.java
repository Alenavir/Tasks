package com.example.task.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Username must be not null.")
    private String Username;

    @NotNull(message = "Password must be not null.")
    private String password;
}
