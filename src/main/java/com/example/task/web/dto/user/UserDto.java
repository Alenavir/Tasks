package com.example.task.web.dto.user;

import com.example.task.web.dto.validation.OnCreate;
import com.example.task.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.sql.Update;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "User Dto")
public class UserDto {

    @NotNull(message = "Id must be not null.", groups = OnUpdate.class)
    @Schema(description = "User id", example = "1")
    private Long id;

    @NotNull(message = "Name must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "User name", example = "Sonya Komorova")
    private String name;

    @NotNull(message = "Username must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "User email", example = "Sonya2001@gmail.com")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // только принимать пароль в Json, но отправлять в Json нам нельзя
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, Update.class})
    @Schema(description = "User encrypted password")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
    @Schema(description = "User password confirmation")
    private String passwordConfirmation;

}
