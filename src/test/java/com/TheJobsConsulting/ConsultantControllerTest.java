package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.ConsultantController;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.service.ConsultantLoginService;
import com.TheJobsConsulting.service.ConsultantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

}





