package com.github.valentina810.weekplannerformarusia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.valentina810.weekplannerformarusia.BaseGeneratorUserResponse;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.service.WeekPlannerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeekPlannerController.class)
class WeekPlannerControllerTest implements BaseGeneratorUserResponse {

    @MockBean
    private WeekPlannerService weekPlannerService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String URL_WEBHOOK = "/webhook";
    private static final MediaType JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String ERROR_BODY = "{" +
            "\"timestamp\": \"2024-03-17T21:44:41.400+00:00\"," +
            "\"status\": 500," +
            "\"error\": \"Internal Server Error\"," +
            "\"path\": \"/webhook\"" +
            "}";

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(JSON_UTF8);
        return headers;
    }

    @Test
    public void getCorrectResponse_whenRequestIsCorrect_thenReturnStatusCode200() throws Exception {
        UserResponse userResponse = createUserResponse();

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mapper.writeValueAsString(userResponse), createJsonHeaders(), HttpStatus.OK);
        when(weekPlannerService.getResponse(any())).thenReturn(responseEntity);

        mvc.perform(post(URL_WEBHOOK)
                        .content(mapper.writeValueAsString(new UserRequest()))
                        .headers(createJsonHeaders()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.text", is(userResponse.getResponse().getText())))
                .andExpect(jsonPath("$.response.tts", is(userResponse.getResponse().getTts())))
                .andExpect(jsonPath("$.response.end_session", is(userResponse.getResponse().isEnd_session())))
                .andExpect(jsonPath("$.session.user_id", is(userResponse.getSession().getUser_id())))
                .andExpect(jsonPath("$.session.session_id", is(userResponse.getSession().getSession_id())))
                .andExpect(jsonPath("$.session.message_id", is(userResponse.getSession().getMessage_id())))
                .andExpect(jsonPath("$.version", is(userResponse.getVersion())))
                .andExpect(jsonPath("$.user_state_update", notNullValue()))
                .andExpect(jsonPath("$.session_state", notNullValue()));

        verify(weekPlannerService, times(1)).getResponse(any());
    }

    @Test
    @SneakyThrows
    public void getError_whenRequestIsNotCorrect_thenReturnStatusCode500() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(ERROR_BODY, createJsonHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        when(weekPlannerService.getResponse(any())).thenReturn(responseEntity);

        mvc.perform(post(URL_WEBHOOK)
                        .content(mapper.writeValueAsString(new UserRequest()))
                        .headers(createJsonHeaders()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.error", notNullValue()))
                .andExpect(jsonPath("$.path", notNullValue()));

        verify(weekPlannerService, times(1)).getResponse(any());
    }
}