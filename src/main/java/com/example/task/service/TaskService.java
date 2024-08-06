package com.example.task.service;

import com.example.task.domain.task.Task;

import java.time.Duration;
import java.util.List;

public interface TaskService {

    Task getById(Long id);
    List<Task> getAllByUserId(Long userId);
    List<Task> getAllTasksForWeek(Long userId);
    List<Task> getAllSoonTasks(final Duration duration);
    Task update(Task task);
    Task create(Task task, Long userId);
    void delete(Long taskId);

}
