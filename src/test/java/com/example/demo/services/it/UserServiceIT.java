package com.example.demo.services.it;


import com.example.demo.entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers()
@SpringBootTest
public class UserServiceIT {

    @Autowired
    UserService userService;

    static String ID = "1234";

    static String NAME = "name";

    static int AGE = 18;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterEach
    void tearDown() {
        userService.deleteAll();
    }

    @Test
    public void shouldSaveUser() {
        User user = new User(ID, NAME, AGE);

        userService.saveUser(user);
        Assertions.assertEquals(userService.getUserById(ID), user);
    }

    @Test
    public void shouldReturnAllUser() {
        User first_user = new User(ID, NAME, AGE);
        User second_user = new User("2345", NAME, AGE);

        List<User> users = List.of(first_user, second_user);

        userService.saveUser(first_user);
        userService.saveUser(second_user);

        Assertions.assertEquals(userService.getAllUsers(), users);

    }

    @Test
    public void shouldUpdateUser() {
        User user = new User(ID, NAME, AGE);

        userService.saveUser(user);

        user.setName("updateUser");
        user.setAge(39);

        userService.saveUser(user);

        Assertions.assertEquals(userService.getUserById(ID), user);

    }

    @Test
    public void shouldDeleteUserById() {
        User user = new User(ID, NAME, AGE);

        userService.saveUser(user);

        Assertions.assertEquals(userService.getAllUsers().size(), 1);

        userService.deleteUserById(ID);

        Assertions.assertEquals(userService.getAllUsers().size(), 0);

    }

}
