package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.AdminController;
import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
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

import java.util.ArrayList;
import java.util.List;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestRegisterConsultant() throws ConsultantException, LoginException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("admin");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        Consultant consultant = new Consultant();
        when(adminService.registerConsultant(any(Consultant.class))).thenReturn(consultant);

        ResponseEntity<Consultant> responce = adminController.registerConsultant("valid_key", consultant);
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals(consultant, responce.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verify(adminService).registerConsultant(consultant);
    }

    @Test
    public void TestRegisterConsultant_InvalidKey() throws LoginException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(false);

        try {
            adminController.registerConsultant("invalid_key", new Consultant());
        } catch (LoginException | ConsultantException e) {
            assertEquals("Invalid; Please Login", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("invalid_key");
        verifyNoInteractions(userService);
        verifyNoInteractions(adminService);
    }

    @Test
    public void testGetAllValidInvalidConsultants() throws LoginException, ConsultantException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);

        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("admin");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        List<Consultant> consultantList = new ArrayList<>();
        when(adminService.getAllValidInvalidConsultants(anyString())).thenReturn(consultantList);

        ResponseEntity<List<Consultant>> response = adminController.getAllConsultants("valid_key");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(consultantList, response.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verify(adminService).getAllValidInvalidConsultants("valid_key");
    }

    @Test
    public void testGetAllValidInvalidConsultants_InvalidUserType() throws LoginException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("user");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        try {
            adminController.getAllConsultants("valid_key");
        } catch (LoginException | ConsultantException e) {
            assertEquals("Please Login as an Admin.", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verifyNoInteractions(adminService);
    }

    @Test
    public void testGetAllUsers() throws LoginException, UserException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);
        List<User> userList = new ArrayList<>();
        when(adminService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = adminController.getALlUsers("valid_key");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(userList, response.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(adminService).getAllUsers();
    }

    @Test
    public void testGetAllUsers_InvalidKey() throws LoginException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(false);

        try {
            adminController.getALlUsers("invalid_key");
        } catch (LoginException | UserException e) {
            assertEquals("Error. Please Login. ", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("invalid_key");
        verifyNoInteractions(adminService);
    }

    @Test
    public void testGetAllAppointments() throws LoginException, AppointmentException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);

        List<Appointment> appointmentList = new ArrayList<>();
        when(adminService.getAllAppointments()).thenReturn(appointmentList);

        ResponseEntity<List<Appointment>> response = adminController.getAllAppointment("valid_key");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(appointmentList, response.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(adminService).getAllAppointments();
    }

    @Test
    public void testGetAllAppointments_InvalidKey() throws LoginException {
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(false);

        try {
            adminController.getAllAppointment("invalid_key");
        } catch (LoginException | AppointmentException e) {
            assertEquals("Please Login First.", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("invalid_key");
        verifyNoInteractions(adminService);
    }

    @Test
    public void testRevokePermissionConsultant() throws LoginException, ConsultantException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("admin");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        Consultant consultant = new Consultant();

        when(adminService.revokePermissionConsultant(any(Consultant.class))).thenReturn(consultant);

        ResponseEntity<Consultant> response = adminController.revokePermissionConsultant("valid_key", consultant);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(consultant, response.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verify(adminService).revokePermissionConsultant(consultant);
    }

    @Test
    public void testRevokePermissionConsultant_InvalidKey() throws LoginException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(false);

        try {
            adminController.revokePermissionConsultant("invalid_key", new Consultant());
        }catch (LoginException | ConsultantException e){
            assertEquals("Please Enter Valid Key.", e.getMessage());
        }
        verify(userAdminLoginService).checkUserLogin("invalid_key");
        verifyNoInteractions(userService);
        verifyNoInteractions(adminService);
    }

    @Test
    public void testRevokePermissionConsultant_InvalidUserType() throws  LoginException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);

        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("user");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        try {
            adminController.revokePermissionConsultant("valid_key", new Consultant());
        }catch (LoginException  |  ConsultantException e){
            assertEquals("Please Login As An Admin.", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verifyNoInteractions(adminService);
    }

    @Test
    public void testConsultantGrantPermission () throws LoginException, ConsultantException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);

        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("admin");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        Consultant consultant = new Consultant();
        when(adminService.grantPermissionConsultant(any(Consultant.class))).thenReturn(consultant);

        ResponseEntity<Consultant> response = adminController.grantPermissionConsultant("valid_key", consultant );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(consultant, response.getBody());

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verify(adminService).grantPermissionConsultant(consultant);
    }

    @Test
    public void testConsultantGrantPermission_InvalidKey () throws LoginException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(false);

        try{
            adminController.grantPermissionConsultant("invalid_key", new Consultant());
        }catch (LoginException | ConsultantException e){
            assertEquals("Please Enter Valid Key.", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("invalid_key");
        verifyNoInteractions(userService);
        verifyNoInteractions(adminService);
    }

    @Test
    public void testConsultantGrantPermission_InvalidUserType() throws LoginException{
        when(userAdminLoginService.checkUserLogin(anyString())).thenReturn(true);

        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("user");
        when(userService.getCurrentUserByUuid(anyString())).thenReturn(currentUserSession);

        try{
            adminController.grantPermissionConsultant("valid_key", new Consultant());
        }catch (LoginException | ConsultantException e){
            assertEquals("Please Login As An Admin.", e.getMessage());
        }

        verify(userAdminLoginService).checkUserLogin("valid_key");
        verify(userService).getCurrentUserByUuid("valid_key");
        verifyNoInteractions(adminService);
    }

}





















