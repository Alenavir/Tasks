package com.example.task.web.controller;

import com.example.task.domain.task.Task;
import com.example.task.service.TaskService;
import com.example.task.web.dto.task.TaskDto;
import com.example.task.web.dto.validation.OnUpdate;
import com.example.task.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get taskDto by id")
    @PreAuthorize("@cse.canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @Operation(summary = "Update task")
    @PreAuthorize("@cse.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task updateTask = taskService.update(task);
        return taskMapper.toDto(updateTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("@cse.canAccessTask(#id)")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
