package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.UserController;
import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.*;
import com.TheJobsConsulting.service.ConsultantService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import com.TheJobsConsulting.service.UserService;
import jakarta.mail.MessagingException;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserAdminLoginService userAdminLoginService;

    @Mock
    private ConsultantService consultantService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveuser()throws UserException{
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.saveUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(user, response.getBody());
        verify(userService).createUser(user);
    }

    @Test
    public void testUpdateUser()throws UserException{
        User user  = new User();
        when(userService.updateUser(user, "valid_key")).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user, "valid_key");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(user, response.getBody());
        verify(userService).updateUser(user, "valid_key");
    }

    @Test
    public void testGetUserDetails() throws LoginException, UserException{
        String key = "valid_key";
        User user = new User();
        when(userAdminLoginService.checkUserLogin(key)).thenReturn(true);
        when(userService.getUserDetails(key)).thenReturn(user);

        ResponseEntity<User> response = userController.viewUserDetails(key);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(user, response.getBody());

        verify(userAdminLoginService).checkUserLogin(key);
        verify(userService).getUserDetails(key);
    }

    @Test
    public void testBookAppointment() throws LoginException, AppointmentException, ConsultantException, IOException,
            TimeDateException, MessagingException{
        String key = "valid_key";
        Appointment appointment = new Appointment();
        when(userAdminLoginService.checkUserLogin(key)).thenReturn(true);
        when(userService.bookAppointment(key, appointment)).thenReturn(appointment);
        ResponseEntity<Appointment> response = userController.bookAppointment(key, appointment);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointment, response.getBody());

        verify(userAdminLoginService).checkUserLogin(key);
        verify(userService).bookAppointment(key, appointment);
    }

    @Test
    public void testAvailableTimeOfConsultant() throws IOException, TimeDateException, LoginException, ConsultantException {
        String key = "valid_key";
        Consultant consultant = new Consultant();
        List<LocalDateTime> availableTiming = new ArrayList<>();
        when(userAdminLoginService.checkUserLogin(key)).thenReturn(true);
        when(consultantService.consultantAvailableTimeForBooking(key, consultant)).thenReturn(availableTiming);
        ResponseEntity<List<LocalDateTime>> response = userController.getConsultantAvailableTime(key, consultant);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertSame(availableTiming, response.getBody());

        verify(userAdminLoginService).checkUserLogin(key);
        verify(consultantService).consultantAvailableTimeForBooking(key, consultant);
    }

}





   


