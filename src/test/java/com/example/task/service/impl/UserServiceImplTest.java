package com.example.task.service.impl;

import com.example.task.config.TestConfig;
import com.example.task.domain.MailType;
import com.example.task.domain.exception.ResourceNotFoundException;
import com.example.task.domain.user.Role;
import com.example.task.domain.user.User;
import com.example.task.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder testPasswordEncoder;
    @MockBean
    private MailServiceImpl mailService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User userTest = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, userTest);
    }

    @Test
    void getByIdReturnException() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        String username = "Kata";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User userTest = userService.getByUsername(username);
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, userTest);
    }

    @Test
    void getByUsernameReturnException() {
        String username = "Kata";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void update() {
        Long id = 1L;
        String password = "password";
        User user = new User();
        user.setId(id);
        user.setPassword(password);

        Mockito.when(testPasswordEncoder.encode(password)).thenReturn("encoderPassword");
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User updated = userService.update(user);
        Mockito.verify(testPasswordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);

        Assertions.assertEquals(user.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getName(), updated.getName());
    }

    @Test
    void create() {
        String username = "kata@gmail.com";
        String password = "password";
        String name = "kata";
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(testPasswordEncoder.encode(password)).thenReturn("encoderPassword");
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        User save = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(mailService).sendEmail(
                user,
                MailType.REGISTRATION,
                new Properties()
        );
        Assertions.assertEquals("encoderPassword", save.getPassword());
        Assertions.assertEquals(Set.of(Role.ROLE_USER), save.getRoles());
    }

    @Test
    void createReturnExceptionFoundByUsername() {
        String username = "kata@gmail.com";
        String password = "password";
        String name = "kata";
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(testPasswordEncoder.encode(password)).thenReturn("encoderPassword");
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createReturnExceptionByPassword() {
        String username = "kata@gmail.com";
        String password = "password";
        String name = "kata";
        String passwordConfirmation = "pass";
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(testPasswordEncoder.encode(password)).thenReturn("encoderPassword");
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void getTaskAuthor() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();
        user.setId(userId);

        Mockito.when(userRepository.findTaskAuthor(taskId)).thenReturn(Optional.of(user));
        User author = userService.getTaskAuthor(taskId);

        Mockito.verify(userRepository).findTaskAuthor(taskId);
        Assertions.assertEquals(user, author);
    }

    @Test
    void getTaskAuthorReturnExceptionNotFound() {
        Long taskId = 1L;

        Mockito.when(userRepository.findTaskAuthor(taskId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getTaskAuthor(taskId));
        Mockito.verify(userRepository).findTaskAuthor(taskId);
    }

    @Test
    void isTaskOwnerReturnTrue() {
        Long userId = 1L;
        Long taskId = 1L;

        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(true);
        Assertions.assertTrue(userService.isTaskOwner(userId, taskId));
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
    }

    @Test
    void isTaskOwnerReturnFalse() {
        Long userId = 1L;
        Long taskId = 1L;

        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(false);
        Assertions.assertFalse(userService.isTaskOwner(userId, taskId));
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }
}
