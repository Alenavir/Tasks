package com.example.task.service.impl;

import com.example.task.config.TestConfig;
import com.example.task.domain.MailType;
import com.example.task.domain.user.User;
import com.example.task.repository.TaskRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Configuration configuration;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailServiceImpl mailService;

    @Test
    void sendEmailForRegistration() {
        try {
            String name = "Kata";
            String username = "kata@gmail.com";

            User user = new User();
            user.setName(name);
            user.setUsername(username);

            Mockito.when(javaMailSender.createMimeMessage())
                    .thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("register.html"))
                    .thenReturn(Mockito.mock(Template.class));
            mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
            Mockito.verify(configuration).getTemplate("register.html");
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void sendEmailForRemind() {
        try {
            String name = "Kata";
            String username = "kata@gmail.com";
            User user = new User();
            user.setName(name);
            user.setUsername(username);

            Mockito.when(javaMailSender.createMimeMessage())
                    .thenReturn(Mockito.mock(MimeMessage.class));
            Mockito.when(configuration.getTemplate("reminder.html"))
                    .thenReturn(Mockito.mock(Template.class));

            mailService.sendEmail(user, MailType.REMINDER, new Properties());
            Mockito.verify(configuration).getTemplate("reminder.html");
            Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
