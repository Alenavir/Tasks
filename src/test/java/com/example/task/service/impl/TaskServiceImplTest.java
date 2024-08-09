package com.example.task.service.impl;

import com.example.task.config.TestConfig;
import com.example.task.domain.exception.ResourceNotFoundException;
import com.example.task.domain.task.Status;
import com.example.task.domain.task.Task;
import com.example.task.repository.TaskRepository;
import com.sun.source.tree.ModuleTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Assertions.assertEquals(task, taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getByIdReturnExceptionNotFound() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = Stream.of(new Task(), new Task())
                        .collect(Collectors.toList());
        Mockito.when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        Assertions.assertEquals(tasks, taskService.getAllByUserId(userId));
        Mockito.verify(taskRepository).findAllByUserId(userId);
    }

    @Test
    void update() {
        Long id = 1L;
        String title = "title";
        Task task = new Task();
        task.setTitle(title);
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task update = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, update);
        Assertions.assertEquals(task.getTitle(), update.getTitle());
        Assertions.assertEquals(task.getStatus(), update.getStatus());
        Assertions.assertEquals(task.getDescription(), update.getDescription());
    }

    @Test
    void updateReturnWithNullStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task update = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, update);
        Assertions.assertEquals(task.getTitle(), update.getTitle());
        Assertions.assertEquals(Status.TODO, update.getStatus());
        Assertions.assertEquals(task.getDescription(), update.getDescription());
    }

    @Test
    void getAllTasksForWeek() {
        Long userId = 1L;
        Task task1 = new Task();
        task1.setTitle("English");
        Task task2 = new Task();
        LocalDateTime dateTime = LocalDateTime.of(2024, 8, 11, 12, 0);  // 2024-08-11 12:00
        task2.setLocalDateTime(dateTime);

        Mockito.when(taskRepository.findAllTasksForWeek(eq(userId), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasksForWeek(userId);

        Mockito.verify(taskRepository).findAllTasksForWeek(eq(userId), any(Timestamp.class), any(Timestamp.class));
        Assertions.assertEquals(2, tasks.size());
        Assertions.assertEquals("English", tasks.get(0).getTitle());
        Assertions.assertEquals(dateTime, tasks.get(1).getLocalDateTime());

    }

    @Test
    void getAllTasksForWeekReturnNull() {
        Long userId = 1L;

        Task task = new Task();
        LocalDateTime dateTime = LocalDateTime.of(2025, 8, 11, 12, 0);  // 2025-08-11 12:00
        task.setLocalDateTime(dateTime);

        Mockito.when(taskRepository.findAllTasksForWeek(
                        eq(userId),
                        any(Timestamp.class),
                        any(Timestamp.class)))
                .thenReturn(Arrays.asList());

        List<Task> tasks = taskService.getAllTasksForWeek(userId);

        Mockito.verify(taskRepository).findAllTasksForWeek(
                eq(userId),
                any(Timestamp.class),
                any(Timestamp.class));
        Assertions.assertEquals(0, tasks.size());
    }
}
