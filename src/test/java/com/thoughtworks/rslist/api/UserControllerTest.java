package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.test_repository.TestRepository;
import com.thoughtworks.rslist.bo.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private RsRepository rsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestRepository testRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private int notExistingId;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRepository.saveAll(Arrays.asList(
                new UserPO("user1", 20, "male", "user1@test.com", "18888888888"),
                new UserPO("user2", 20, "female", "user2@test.com", "18888888888"),
                new UserPO("user3", 20, "female", "user3@test.com", "18888888888")
        ));
        notExistingId = testRepository.getNextId();
    }

    @Test
    @Order(2)
    void should_get_user_list() throws Exception {
        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888")
        ));
        String jsonString = objectMapper.writeValueAsString(userList);

        mockMvc.perform(get("/users"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void should_add_user_with_correct_format() throws Exception {
        User newUser = new User("newUser", 20, "male", "new@test.com", "18888888888");
        String jsonString = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().longValue("id", userRepository.findIdByUserName("newUser")));;

        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888"),
                newUser
        ));
        jsonString = objectMapper.writeValueAsString(userList);

        mockMvc.perform(get("/users"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_refuse_add_user_given_wrong_format_user() throws Exception {
        User tooLangNameUser = new User("name_too_lang", 20, "male", "new@test.com", "18888888888");
        User tooYoungUser = new User("newUser", 15, "male", "new@test.com", "18888888888");
        User wrongEmailUser = new User("newUser", 20, "male", "wrongEmail.com", "18888888888");
        User wrongPhoneUser = new User("newUser", 20, "male", "new@test.com", "110");


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

    @Test
    void should_return_all_users_and_rename_fields() throws Exception {
        String expectJson = objectMapper.writeValueAsString(Arrays.asList(
                new User("user1", 20, "male", "user1@test.com", "18888888888"),
                new User("user2", 20, "female", "user2@test.com", "18888888888"),
                new User("user3", 20, "female", "user3@test.com", "18888888888")
        ));

        mockMvc.perform(get("/users"))
                .andExpect(content().json(expectJson))
                .andExpect(jsonPath("$[0].user_name", is("user1")))
                .andExpect(jsonPath("$[0].user_age", is(20)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is("user1@test.com")))
                .andExpect(jsonPath("$[0].user_phone", is("18888888888")))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_user_given_user_id() throws Exception {
        String expect = objectMapper.writeValueAsString(new User("user1", 20, "male", "user1@test.com", "18888888888"));

        mockMvc.perform(get("/user/{userId}", userRepository.findIdByUserName("user1")))
                .andExpect(content().json(expect))
                .andExpect(status().isOk());
    }

    @Test
    void should_throw_when_get_user_given_wrong_user_id() throws Exception {
        mockMvc.perform(get("/user/{userId}", notExistingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user id")));
    }

    @Test
    void should_remove_user_given_user_id() throws Exception {
        int userId = userRepository.findIdByUserName("user1");

        mockMvc.perform(delete("/user/{userId}", userId)).andExpect(status().isOk());

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    void should_throw_when_delete_user_given_wrong_user_id() throws Exception {
        mockMvc.perform(delete("/user/{userId}", notExistingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user id")));
    }

    @Test
    void should_remove_user_and_related_events_given_user_id() throws Exception {
        UserPO userPO = userRepository.findByUserName("user1").orElse(new UserPO());
        int userId = userPO.getId();
        RsEventPO rsEventPO = RsEventPO.builder().eventName("whatever").keyWord("whatever").userPO(userPO).build();
        rsRepository.save(rsEventPO);
        int rsEventId = rsEventPO.getId();

        mockMvc.perform(delete("/user/{userId}", userId)).andExpect(status().isOk());

        assertFalse(userRepository.existsById(userId));
        assertFalse(rsRepository.existsById(rsEventId));
    }
}