package com.example.task.web.dto.task;

import com.example.task.domain.task.Status;
import com.example.task.web.dto.validation.OnCreate;
import com.example.task.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Schema(description = "Task Dto")
public class TaskDto {

    @NotNull(message = "Id must be not null.", groups = OnUpdate.class)
    @Schema(description = "Task id", example = "1")
    private Long id;

    @NotNull(message = "Title must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Task title", example = "Do English")
    private String title;

    @Length(max = 255, message = "Description must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Task description", example = "Parse the past simple")
    private String description;

    @Schema(description = "Task status", example = "TODO")
    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Task completion time", example = "2024-07-21 00:00")
    private LocalDateTime localDateTime;

}
