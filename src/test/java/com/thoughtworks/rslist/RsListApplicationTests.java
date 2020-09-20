package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
        List<String> rsList = Arrays.asList("第一条事件", "第二条事件", "第三条事件");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(get("/list"))
                .andExpect(content().json(objectMapper.writeValueAsString(rsList)))
                .andExpect(status().isOk());
    }

}
