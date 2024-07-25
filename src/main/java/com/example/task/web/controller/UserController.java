package com.example.task.web.controller;

import com.example.task.domain.task.Task;
import com.example.task.domain.user.User;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import com.example.task.web.dto.task.TaskDto;
import com.example.task.web.dto.user.UserDto;
import com.example.task.web.dto.validation.OnCreate;
import com.example.task.web.dto.validation.OnUpdate;
import com.example.task.web.mappers.TaskMapper;
import com.example.task.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update user")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updateUser = userService.update(user);
        return userMapper.toDto(updateUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get UserDto by id")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get UserDto by username")
    public UserDto getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all user tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    public TaskDto createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }
}
