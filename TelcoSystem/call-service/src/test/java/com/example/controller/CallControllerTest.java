package com.example.telcosystem.controller;

import com.example.telcosystem.service.CallService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CallControllerTest {

    @Mock
    private CallService callService;

    @InjectMocks
    private CallController callController;

    private MockMvc mockMvc;

    @Test
    void testStartCall() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(callController).build();

        when(callService.startCall("user1", "user2")).thenReturn(true);

        mockMvc.perform(post("/api/call/start")
                .param("user1", "user1")
                .param("user2", "user2"))
                .andExpect(status().isOk());
    }
}
