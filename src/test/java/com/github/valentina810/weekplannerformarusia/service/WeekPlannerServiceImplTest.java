package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.BaseGeneratorUserResponse;
import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class WeekPlannerServiceImplTest implements BaseGeneratorUserResponse {

    @InjectMocks
    private WeekPlannerServiceImpl weekPlannerService;

    @Mock
    private ActionExecutor actionExecutor;

    @Test
    void getResponse_whenRequestBodyNotCorrect_thenReturnRuntimeException() {
        when(actionExecutor.createUserResponse(any())).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class,
                () -> weekPlannerService.getResponse(new Object()));

        verify(actionExecutor, times(1)).createUserResponse(any());
    }

    @Test
    void getResponse_whenRequestBodyCorrect_thenReturnResponse() {
        when(actionExecutor.createUserResponse(any())).thenReturn(createUserResponse());
        ResponseEntity<?> response = weekPlannerService.getResponse(new UserRequest());

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(HEADER_KEY, response.getHeaders().get(HEADER_VALUE).get(0));
        assertEquals(BASE_USER_RESPONSE, response.getBody().toString());

        verify(actionExecutor, times(1)).createUserResponse(any());
    }
}