package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserService userService;

    @BeforeEach
    void setup() {
        userService.init(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888")
        ));
    }

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
    void should_add_user_with_correct_format() throws Exception {
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

    @Test
    void should_refuse_add_user_given_wrong_format_user() throws Exception {
        User tooLangNameUser = new User("name_too_lang", 20, "male", "new@test.com", "18888888888");
        User tooYoungUser = new User("newUser", 15, "male", "new@test.com", "18888888888");
        User wrongEmailUser = new User("newUser", 20, "male", "wrongEmail.com", "18888888888");
        User wrongPhoneUser = new User("newUser", 20, "male", "new@test.com", "110");


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(tooLangNameUser);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
        jsonString = objectMapper.writeValueAsString(tooYoungUser);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
        jsonString = objectMapper.writeValueAsString(wrongEmailUser);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
        jsonString = objectMapper.writeValueAsString(wrongPhoneUser);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


}