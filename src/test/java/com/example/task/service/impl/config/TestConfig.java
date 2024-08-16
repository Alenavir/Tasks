package com.example.task.service.impl.config;

import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import com.example.task.service.impl.AuthServiceImpl;
import com.example.task.service.impl.MailServiceImpl;
import com.example.task.service.impl.TaskServiceImpl;
import com.example.task.service.impl.UserServiceImpl;
import com.example.task.service.props.JwtProperties;
import com.example.task.web.security.JwtTokenProvider;
import com.example.task.web.security.JwtUserDetailsService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg==");
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public MailServiceImpl mailService() {
        return new MailServiceImpl(configuration(),mailSender());
    }

    @Bean
    public JwtTokenProvider tokenProvider(UserRepository userRepository) {
        return new JwtTokenProvider(
                jwtProperties(),
                userDetailsService(userRepository),
                userService(userRepository));
    }

    @Bean
    @Primary
    public UserServiceImpl userService(UserRepository userRepository) {
        return new UserServiceImpl(
                userRepository,
                mailService(),
                testPasswordEncoder()
        );
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(TaskRepository taskRepository) {
        return new TaskServiceImpl(taskRepository);
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(authenticationManager,
                userService(userRepository),
                tokenProvider(userRepository));
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    @Primary
    AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }
}
