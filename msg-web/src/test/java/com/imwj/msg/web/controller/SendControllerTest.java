package com.imwj.msg.web.controller;

import com.imwj.msg.api.domain.MessageParam;
import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;
import com.imwj.msg.api.service.SendService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SendController.class)
class SendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendService mockSendService;

    @Test
    void testSendSmsTest() throws Exception {
        // Setup
        when(mockSendService.send(new SendRequest("code", 0L,
                new MessageParam("receiver", new HashMap<>(), new HashMap<>()))))
                .thenReturn(new SendResponse("code", "msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/sms/sendSmsTest")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSendEmailTest() throws Exception {
        // Setup
        HashMap<String, String> variables = new HashMap<>();
        variables.put("title", "EmailTest");
        variables.put("content", "666677");
        when(mockSendService.send(new SendRequest("send", 2L,
                new MessageParam("2916863213@qq.com", variables, new HashMap<>()))))
                .thenReturn(new SendResponse("code", "msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/sms/sendEmailTest")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSendWechatTest() throws Exception {
        // Setup
        when(mockSendService.send(new SendRequest("code", 0L,
                new MessageParam("receiver", new HashMap<>(), new HashMap<>()))))
                .thenReturn(new SendResponse("code", "msg"));

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/sms/sendWechatTest")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
