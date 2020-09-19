package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.test_repository.TestRepository;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTests {
    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private RsRepository rsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestRepository testRepository;

    private int defaultUserId;
    private int defaultRsEventId;
    private int notExistingId;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        final UserPO defaultUserPO = new UserPO("user1", 20, "male", "user1@test.com", "18888888888");
        userRepository.saveAll(Arrays.asList(
                defaultUserPO,
                new UserPO("user2", 20, "female", "user2@test.com", "18888888888"),
                new UserPO("user3", 20, "female", "user3@test.com", "18888888888")
        ));
        defaultUserId = defaultUserPO.getId();

        rsRepository.deleteAll();
        final RsEventPO defaultRsEventPO = new RsEventPO("第一条事件", "无标签", defaultUserPO);
        rsRepository.saveAll(Arrays.asList(
                defaultRsEventPO,
                new RsEventPO("第二条事件", "无标签", defaultUserPO),
                new RsEventPO("第三条事件", "无标签", defaultUserPO)));
        defaultRsEventId = defaultRsEventPO.getId();

        notExistingId = testRepository.getNextId();
    }

    @Test
    void should_get_all_list() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", defaultUserId),
                new RsEvent("第二条事件", "无标签", defaultUserId),
                new RsEvent("第三条事件", "无标签", defaultUserId)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_sublist() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", defaultUserId),
                new RsEvent("第二条事件", "无标签", defaultUserId)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_can_post_new_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", defaultUserId);
        String json = objectMapper.writeValueAsString(rsEvent);
        final MvcResult mvcResult = mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertReturnedIdValid(rsEvent, mvcResult);

        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第一条事件", "无标签", defaultUserId),
                new RsEvent("第二条事件", "无标签", defaultUserId),
                new RsEvent("第三条事件", "无标签", defaultUserId),
                rsEvent));

        String jsonString = objectMapper.writeValueAsString(rsList);
        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_refuse_when_add_event_given_not_existing_user() throws Exception {
        RsEvent rsEvent = new RsEvent("whatever", "whatever", notExistingId);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_event_given_existing_user() throws Exception {
        RsEvent rsEvent = new RsEvent("whatever", "whatever", defaultUserId);
        String json = objectMapper.writeValueAsString(rsEvent);

        final MvcResult mvcResult = mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertReturnedIdValid(rsEvent, mvcResult);
    }

    @Test
    void should_add_event_given_existing_event() throws Exception {
        RsEvent rsEvent = new RsEvent("whatever", "whatever", defaultUserId);
        String json = objectMapper.writeValueAsString(rsEvent);

        final MvcResult firstMvcResult = mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        final MvcResult secondMvcResult = mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertNotEquals(firstMvcResult.getResponse().getHeader("eventId"),
                secondMvcResult.getResponse().getHeader("eventId"));
    }

    @Test
    void should_refuse_when_add_event_given_null_fields() throws Exception {
        RsEvent nullEventNameEvent = new RsEvent(null, "whatever", defaultUserId);
        RsEvent nullKeyWordEvent = new RsEvent("whatever", null, defaultUserId);

        String nullEventNameJson = objectMapper.writeValueAsString(nullEventNameEvent);
        String nullKeyWordJson = objectMapper.writeValueAsString(nullKeyWordEvent);

        mockMvc.perform(post("/rs/event").content(nullEventNameJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
        mockMvc.perform(post("/rs/event").content(nullKeyWordJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_get_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("第一条事件", "无标签", defaultUserId);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId))
                .andExpect(content().json(json))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_given_all_fields() throws Exception {
        RsEvent rsEvent = new RsEvent("第一条事件（新标签）", "新标签", userRepository.findIdByUserName("user2"));
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId))
                .andExpect(content().json(json))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_given_one_field() throws Exception {
        RsEvent firstTestEvent = new RsEvent();
        firstTestEvent.setKeyWord("只改标签");

        String firstJson = objectMapper.writeValueAsString(firstTestEvent);

        RsEvent firstExpectRsEvent = new RsEvent("第一条事件", "只改标签", defaultUserId);
        String firstExpectJson = objectMapper.writeValueAsString(firstExpectRsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(firstJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId))
                .andExpect(content().json(firstExpectJson))
                .andExpect(status().isOk());

        RsEvent secondTestEvent = new RsEvent();
        secondTestEvent.setEventName("第一条事件（新）");
        String secondJson = objectMapper.writeValueAsString(secondTestEvent);

        RsEvent secondExpectRsEvent = new RsEvent("第一条事件（新）", "只改标签", defaultUserId);
        String secondExpectJson = objectMapper.writeValueAsString(secondExpectRsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(secondJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId))
                .andExpect(content().json(secondExpectJson))
                .andExpect(status().isOk());

        RsEvent thirdTestEvent = new RsEvent();
        thirdTestEvent.setUserId(userRepository.findIdByUserName("user2"));
        String thirdJson = objectMapper.writeValueAsString(thirdTestEvent);

        RsEvent thirdExpectRsEvent = new RsEvent("第一条事件（新）", "只改标签", userRepository.findIdByUserName("user2"));
        String thirdExpectJson = objectMapper.writeValueAsString(thirdExpectRsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(thirdJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId))
                .andExpect(content().json(thirdExpectJson))
                .andExpect(status().isOk());
    }

    @Test
    void should_update_event_given_existing_user() throws Exception {
        RsEvent rsEvent = new RsEvent(null, null, userRepository.findIdByUserName("user2"));
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEvent expectRsEvent = new RsEvent("第一条事件", "无标签", userRepository.findIdByUserName("user2"));
        String expectJson = objectMapper.writeValueAsString(expectRsEvent);
        mockMvc.perform(get("/rs/{eventId}", defaultRsEventId)).andExpect(content().json(expectJson))
                .andExpect(status().isOk());
    }

    @Test
    void should_refuse_when_update_event_given_not_existing_user() throws Exception {
        RsEvent rsEvent = new RsEvent(null, null, notExistingId);
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/{eventId}", defaultRsEventId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_event_given_event_id() throws Exception {
        List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
                new RsEvent("第二条事件", "无标签", defaultUserId),
                new RsEvent("第三条事件", "无标签", defaultUserId)));

        String jsonString = objectMapper.writeValueAsString(rsList);

        mockMvc.perform(delete("/rs/{eventId}", defaultRsEventId)).andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk());
    }

    @Test
    void should_throw_given_out_range_index() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_throw_given_wrong_event_id() throws Exception {
        mockMvc.perform(get("/rs/{wrongId}", notExistingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid event id")));
    }

    private void assertReturnedIdValid(RsEvent rsEvent, MvcResult mvcResult) {
        final int eventId = Integer.parseInt(Objects.requireNonNull(mvcResult.getResponse().getHeader("eventId")));
        RsEventPO rsEventPO = rsRepository.findById(eventId).orElse(new RsEventPO());
        assertEquals(rsEvent, new RsEvent(rsEventPO.getEventName(), rsEventPO.getKeyWord(), rsEventPO.getUserPO().getId()));
    }
}
