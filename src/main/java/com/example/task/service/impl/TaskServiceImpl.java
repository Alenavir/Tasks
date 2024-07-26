package com.example.task.service.impl;

import com.example.task.domain.exception.ResourceNotFoundException;
import com.example.task.domain.task.Status;
import com.example.task.domain.task.Task;
import com.example.task.repository.TaskRepository;
import com.example.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }


    @Override
    @Transactional
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    public List<Task> getAllSoonTasks(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfWeek = now.plusWeeks(1);

        System.out.println(Timestamp.valueOf(now)  + "  -  " + Timestamp.valueOf(endOfWeek));

        return taskRepository.findAllSoonTasks(userId, Timestamp.valueOf(now), Timestamp.valueOf(endOfWeek));
    }


    @Override
    @Transactional
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
