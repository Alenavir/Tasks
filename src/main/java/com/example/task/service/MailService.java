package com.example.task.service;

import com.example.task.domain.MailType;
import com.example.task.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType mailType, Properties properties);

}
