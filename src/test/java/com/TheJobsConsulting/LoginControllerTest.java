package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.LoginController;
import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.service.ConsultantLoginService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserAdminLoginService userAdminLoginService;

    @Mock
    private ConsultantLoginService consultantLoginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginUser()throws LoginException{
        LoginDTO loginDTO = new LoginDTO();
        LoginUUIDKey loginUUIDKey = new LoginUUIDKey();
        when(userAdminLoginService.logToAccount(loginDTO)).thenReturn(loginUUIDKey);

        ResponseEntity<LoginUUIDKey> response = loginController.loginUser(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(loginUUIDKey, response.getBody());

        verify(userAdminLoginService).logToAccount(loginDTO);
    }

    @Test
    public void testLoginConsultant() throws LoginException{
        LoginDTO loginDTO = new LoginDTO();
        LoginUUIDKey loginUUIDKey = new LoginUUIDKey();
        when(consultantLoginService.logToAccount(loginDTO)).thenReturn(loginUUIDKey);
        ResponseEntity<LoginUUIDKey> response = loginController.loginConsultant(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(loginUUIDKey, response.getBody());
        verify(consultantLoginService).logToAccount(loginDTO);
    }

}


