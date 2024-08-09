package com.example.task.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request after login")
public class JwtResponse {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "email", example = "Sonya2001@gmail.com")
    private String Username;

    private String accessToken;
    private String refreshToken;

}
