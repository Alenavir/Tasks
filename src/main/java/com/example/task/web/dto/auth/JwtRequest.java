package com.example.task.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @NotNull(message = "Username must be not null.")
    @Schema(description = "email", example = "Sonya2002@gmail.com")
    private String Username;

    @NotNull(message = "Password must be not null.")
    @Schema(description = "password", example = "123Sd3")
    private String password;
}
