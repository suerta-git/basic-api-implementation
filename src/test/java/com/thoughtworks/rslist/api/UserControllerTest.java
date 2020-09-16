package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(2)
    void should_get_user_list() throws Exception {
        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888")
        ));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userList);

        mockMvc.perform(get("/user"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void should_add_user_without_validation() throws Exception {
        User newUser = new User("newUser", 20, "male", "new@test.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888"),
                newUser
        ));
        jsonString = objectMapper.writeValueAsString(userList);

        mockMvc.perform(get("/user"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }
}