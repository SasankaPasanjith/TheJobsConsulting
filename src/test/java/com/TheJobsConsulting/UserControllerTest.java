package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.UserController;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.service.ConsultantService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import com.TheJobsConsulting.service.UserService;
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

}






