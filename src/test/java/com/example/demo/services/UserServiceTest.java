package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    final static String ID = "1234";

    final static String NAME = "user";

    final static Integer AGE = 30;
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveUser() {
        User user = new User(ID, NAME, AGE);

        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById() {
        User user = new User(ID, NAME, AGE);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.getUserById(ID), user);

    }

    @Test
    void shouldHandleUserNotFound_whenUserNotExist() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows( UserNotFoundException.class, () -> { userService.getUserById(ID);});
    }

    @Test
    void getAllUsers() {
        User user = new User(ID, NAME, AGE);
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        Assertions.assertEquals(userService.getAllUsers(), users);

    }

    @Test
    void shouldReturnEmpyListUser_WhenHasNoUser() {
        when(userRepository.findAll()).thenReturn(List.of());

        Assertions.assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void updateUser() {
        User user = new User(ID, NAME, AGE);
        String NEW_NAME = "new_user";
        Integer NEW_AGE = 34;

        user.setAge(NEW_AGE);
        user.setName(NEW_NAME);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUserById() {
        User user = new User(ID, NAME, AGE);

        userService.deleteUserById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}