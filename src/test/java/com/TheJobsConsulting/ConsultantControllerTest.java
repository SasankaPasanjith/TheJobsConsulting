package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.ConsultantController;
import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.*;
import com.TheJobsConsulting.service.ConsultantLoginService;
import com.TheJobsConsulting.service.ConsultantService;
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

public class ConsultantControllerTest {

    @InjectMocks
    private ConsultantController consultantController;

    @Mock
    private ConsultantLoginService consultantLoginService;

    @Mock
    private ConsultantService consultantService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConsultantDetails ()throws LoginException, UserException{
        String validKey = "valid_key";
        Consultant consultant = new Consultant();
        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.getConsultantDetails(validKey)).thenReturn(consultant);

        ResponseEntity<Consultant> response = consultantController.getConsultantDetails(validKey);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(consultant, response.getBody());
    }

    @Test
    public void testFutureAppointment() throws LoginException, UserException, ConsultantException, AppointmentException {
        String validKey = "valid_key";
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("consultant");
        Consultant registerConsultant = new Consultant();
        List<Appointment> appointments =new ArrayList<>();

        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.getCurrentUserByUuid(validKey)).thenReturn(currentUserSession);
        when(consultantService.getConsultantByUuid(validKey)).thenReturn(registerConsultant);
        when(consultantService.getFutureAppointments(registerConsultant)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = consultantController.getUpcomingAppointments(validKey);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(appointments, response.getBody());
    }

    @Test
    public void testFutureAppointmnet_InvalidKey() throws LoginException {
        String invalidKey = "invalidkey";
        when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);

        assertThrows(LoginException.class, () -> consultantController.getUpcomingAppointments(invalidKey));
    }

    @Test
    public void testGetPastAppointment () throws ConsultantException, LoginException, AppointmentException, UserException{
        String validKey = "valid_key";
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("consultant");
        Consultant registerConsultant = new Consultant();
        List<Appointment> appointments = new ArrayList<>();

        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.getCurrentUserByUuid(validKey)).thenReturn(currentUserSession);
        when(consultantService.getConsultantByUuid(validKey)).thenReturn(registerConsultant);
        when(consultantService.getPastAppointments(registerConsultant)).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = consultantController.getPastAppointments(validKey);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(appointments, response.getBody());
    }

    @Test
    public void testGetPastAppointment_InvalidKey() throws  LoginException{
        String invalidKey = "invalidKey";
        when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);
        assertThrows(LoginException.class, () -> consultantController.getPastAppointments(invalidKey));
    }

    @Test
    public void testGetAllAppointments() throws LoginException, UserException, AppointmentException, ConsultantException{

       String validKey = "validKey";
       CurrentSession currentUserSession = new CurrentSession();
       currentUserSession.setUserType("consultant");
       Consultant registerConsultant = new Consultant();
       List<Appointment> appointments = new ArrayList<>();

       when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
       when(consultantService.getCurrentUserByUuid(validKey)).thenReturn(currentUserSession);
       when(consultantService.getConsultantByUuid(validKey)).thenReturn(registerConsultant);
       when(consultantService.getAllAppointments(registerConsultant)).thenReturn(appointments);

       ResponseEntity<List<Appointment>> response = consultantController.getAllAppointments(validKey);

       assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
       assertSame(appointments, response.getBody());
    }

    @Test
    public void testGetAllAppointments_InvalidKey () throws  LoginException{
        String invalidKey = "invalidKey";
        when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);
        assertThrows(LoginException.class, () -> consultantController.getAllAppointments(invalidKey));
    }

    @Test
    public void testGetAllListOfUsers() throws ConsultantException, LoginException, UserException{
        String validKey = "validKey";
        CurrentSession currentUserSession = new CurrentSession();
        currentUserSession.setUserType("consultant");
        Consultant registerConsultant = new Consultant();
        List<User> users = new ArrayList<>();

        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.getCurrentUserByUuid(validKey)).thenReturn(currentUserSession);
        when(consultantService.getConsultantByUuid(validKey)).thenReturn(registerConsultant);
        when(consultantService.getUserList()).thenReturn(users);

        ResponseEntity<List<User>> response = consultantController.getListOfUsers(validKey);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(users, response.getBody());
    }

    @Test
    public void testGetAllListOfUsers_InvalidKey() throws LoginException{
        String invalidKey = "invalidKey";
        when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);
        assertThrows(LoginException.class, () -> consultantController.getListOfUsers(invalidKey));
    }

    @Test
    public void testForgotPassword() throws LoginException, PasswordException{
        String validKey ="validKey";
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setCurrentPassword("current_password");
        forgotPassword.setNewPassword("new_password");
        forgotPassword.setConfirmNewPassword("new_password");
        Consultant consultant = new Consultant();

        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.forgotPassword(validKey, forgotPassword)).thenReturn(consultant);

        ResponseEntity<Consultant> response = consultantController.forgotPassword(validKey, forgotPassword);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(consultant, response.getBody());
    }

    @Test
    public void testForgotPassword_InvalidKey() throws  LoginException{
      String invalidKey ="invalidKey";
      ForgotPassword forgotPassword = new ForgotPassword();
      when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);
      assertThrows(LoginException.class, ()-> consultantController.forgotPassword(invalidKey, forgotPassword));
    }

    @Test
    public void testTimeUpdate() throws  LoginException, ConsultantException{
        String validKey = "validKey";
        UpdateTime updateTime = new UpdateTime();
        updateTime.setAppointmentStartTime(9);
        updateTime.setAppointmentEndTime(10);
        Consultant consultant = new Consultant();

        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);
        when(consultantService.updateTime(validKey, updateTime)).thenReturn(consultant);

        ResponseEntity<Consultant> response = consultantController.timeUpdate(validKey, updateTime);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(consultant, response.getBody());
    }

    @Test
    public void testTimeUpdate_validKey_InvalidTime ()throws LoginException{
        String validKey = "validkey";
        UpdateTime updateTime = new UpdateTime();
        updateTime.setAppointmentStartTime(10);
        updateTime.setAppointmentEndTime(9);
        when(consultantLoginService.checkUserLogin(validKey)).thenReturn(true);

        assertThrows(ConsultantException.class, ()->consultantController.timeUpdate(validKey, updateTime));
    }

    @Test
    public void testTimeUpdate_InvalidKey ()throws LoginException, ConsultantException{
        String invalidKey = "invalidKey";
        UpdateTime updateTime = new UpdateTime();
        when(consultantLoginService.checkUserLogin(invalidKey)).thenReturn(false);

        assertThrows(LoginException.class, ()->consultantController.timeUpdate(invalidKey, updateTime));
    }

}












