package com.example.task.service;

import com.example.task.domain.task.Task;
import com.example.task.domain.user.User;

import java.util.List;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    User getTaskAuthor(Long taskId);

    void delete(Long id);
}
