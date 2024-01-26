package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.AdminController;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.service.AdminService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import com.TheJobsConsulting.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Mock
    private UserAdminLoginService userAdminLoginService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerConsultant() throws ConsultantException, LoginException {
        when (userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("admin");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        Consultant consultant = new Consultant();
        when (adminService.registerConsultant(any (Consultant.class))).thenReturn(consultant);

        ResponseEntity<Consultant> responce = adminController.registerConsultant("valid_key", consultant);
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals(consultant, responce.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verify(adminService).registerConsultant(consultant);
    }



    @Test
    void getAllConsultants() {
    }

    @Test
    void getALlUsers() {
    }

    @Test
    void getAllAppointment() {
    }

    @Test
    void revokePermissionConsultant() {
    }

    @Test
    void grantPermissionConsultant() {
    }
}