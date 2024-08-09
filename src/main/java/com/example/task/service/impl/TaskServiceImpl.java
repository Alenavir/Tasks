package com.example.task.service.impl;

import com.example.task.domain.exception.ResourceNotFoundException;
import com.example.task.domain.task.Status;
import com.example.task.domain.task.Task;
import com.example.task.repository.TaskRepository;
import com.example.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Cacheable(
            value = "TaskService::getById",
            key = "#id"
    )
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }


    @Override
    @Transactional
    @CachePut(
            value = "TaskService::getById",
            key = "#task.id"
    )
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    public List<Task> getAllTasksForWeek(Long userId) {
        ZonedDateTime nowZoned = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime end = now.plusWeeks(1);

        Timestamp startTimestamp = Timestamp.valueOf(now);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        return taskRepository.findAllTasksForWeek(userId, startTimestamp, endTimestamp);
    }

    @Override
    @Transactional
    @Cacheable(
            value = "TaskService::getById",
            condition = "#task.id!=null",
            key = "#task.id"
    )
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "TaskService::getById",
            key = "#id"
    )
    public void delete(Long id) {
        taskRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAllSoonTasks(final Duration duration) {
        ZonedDateTime nowZoned = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime end = now.plus(duration);

        Timestamp startTimestamp = Timestamp.valueOf(now);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        return taskRepository.findAllSoonTasks(startTimestamp, endTimestamp);
    }

}
