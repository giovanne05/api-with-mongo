package com.example.demo.services.it;


import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

    @Test
    public void shouldSaveUser() {
        User user = new User(ID, NAME, AGE);

        userService.saveUser(user);
        User userById = userService.getUserById(ID);

        Assertions.assertEquals(userById, user);
    }
}
