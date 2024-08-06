package com.example.task.service.impl;

import com.example.task.domain.MailType;
import com.example.task.domain.task.Task;
import com.example.task.domain.user.User;
import com.example.task.service.MailService;
import com.example.task.service.ReminderService;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration duration = Duration.ofDays(1);

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @Override
    public void remindForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(duration);
        System.out.println(tasks);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            System.out.println(user);
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription() != null ? task.getDescription() : "");
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }
}
