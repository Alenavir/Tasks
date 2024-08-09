package com.example.task.service.impl;

import com.example.task.domain.MailType;
import com.example.task.domain.user.User;
import com.example.task.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(User user, MailType mailType, Properties properties) {
        switch (mailType) {
            case REGISTRATION -> sendRegistrationEmail(user, properties);
            case REMINDER -> sendReminderEmail(user, properties);
            default -> {}
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(User user, Properties properties) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setSubject("Thank you for registration " + user.getName());
            helper.setTo(user.getUsername());
            String emailContent = getRegistrationEmailContent(user, properties);
            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при отправке электронного письма", e);
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Неизвестная ошибка", e);
        }
    }


    @SneakyThrows
    private String getRegistrationEmailContent(User user, Properties properties) {
        try {
            StringWriter writer = new StringWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            configuration.getTemplate("register.html").process(model, writer);
            return writer.getBuffer().toString();
        } catch (IOException | TemplateException e) {
            System.err.println("Ошибка обработки шаблона: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Ошибка при генерации содержимого электронного письма", e);
        }
    }

    @SneakyThrows
    private void sendReminderEmail(User user, Properties properties) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("You have task to do in 1 hour");
        helper.setTo(user.getUsername());
        String emailContext = getReminderEmailContent(user, properties);
        helper.setText(emailContext, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getReminderEmailContent(User user, Properties properties) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.html")
                .process(model, writer);
        return writer.getBuffer().toString();
    }
}
