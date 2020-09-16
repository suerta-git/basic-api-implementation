package com.thoughtworks.rslist;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RsListApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Test
    void should_get_all_list() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第二条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第三条事件", "无标签", RsController.DEFAULT_USER)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_sublist() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第二条事件", "无标签", RsController.DEFAULT_USER)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_can_post_new_event() throws Exception {
        User user1 = new User("user1", 20, "male", "default@test.com", "10987654321");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user1);
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第二条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第三条事件", "无标签", RsController.DEFAULT_USER),
                rsEvent));

        String jsonString = objectMapper.writeValueAsString(rsList);
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("第一条事件", "无标签", RsController.DEFAULT_USER);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(get("/rs/1"))
                .andExpect(content().json(json))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_given_two_fields() throws Exception {
        RsEvent rsEvent = new RsEvent("第一条事件（新标签）", "新标签", RsController.DEFAULT_USER);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/1"))
                .andExpect(content().json(json))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_given_one_field() throws Exception {
        RsEvent firstTestEvent = new RsEvent();
        firstTestEvent.setKeyWord("只改标签");

        String firstJson = objectMapper.writeValueAsString(firstTestEvent);

        RsEvent firstExpectRsEvent = new RsEvent("第一条事件", "只改标签", RsController.DEFAULT_USER);
        String firstExpectJson = objectMapper.writeValueAsString(firstExpectRsEvent);

        mockMvc.perform(patch("/rs/1").content(firstJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().json(firstExpectJson))
                .andExpect(status().isOk());

        RsEvent secondTestEvent = new RsEvent();
        secondTestEvent.setEventName("第一条事件（新）");
        String secondJson = objectMapper.writeValueAsString(secondTestEvent);

        RsEvent secondExpectRsEvent = new RsEvent("第一条事件（新）", "只改标签", RsController.DEFAULT_USER);
        String secondExpectJson = objectMapper.writeValueAsString(secondExpectRsEvent);

        mockMvc.perform(patch("/rs/1").content(secondJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().json(secondExpectJson))
                .andExpect(status().isOk());

        RsEvent thirdTestEvent = new RsEvent();
        User newUser = new User("newUser", 20, "male", "default@test.com", "10987654321");
        thirdTestEvent.setUser(newUser);
        String thirdJson = objectMapper.writeValueAsString(thirdTestEvent);

        RsEvent thirdExpectRsEvent = new RsEvent("第一条事件（新）", "只改标签", newUser);
        String thirdExpectJson = objectMapper.writeValueAsString(thirdExpectRsEvent);

        mockMvc.perform(patch("/rs/1").content(thirdJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(content().json(thirdExpectJson))
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_event_given_index() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第二条事件", "无标签", RsController.DEFAULT_USER),
                new RsEvent("第三条事件", "无标签", RsController.DEFAULT_USER)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_refuse_when_add_event_given_user_with_wrong_format() throws Exception {
        User tooLangNameUser = new User("too_lang_name", 20, "male", "test@test.com", "10987654321");
        RsEvent rsEvent = new RsEvent("whatever", "whatever", tooLangNameUser);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_new_user_when_add_event_given_new_user() throws Exception {
        User newUser = new User("newUser", 80, "female", "someone@test.com", "11234567890");
        RsEvent rsEvent = new RsEvent("whatever", "whatever", newUser);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(userService.contains(newUser));
    }

    @Test
    void should_not_add_user_when_add_event_given_existing_user() throws Exception {
        User existingUser = new User("user1", 80, "female", "someone@test.com", "11234567890");
        RsEvent rsEvent = new RsEvent("whatever", "whatever", existingUser);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(userService.contains(existingUser));
    }

}
