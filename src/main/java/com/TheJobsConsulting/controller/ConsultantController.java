package com.TheJobsConsulting.controller;


import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.service.ConsultantLoginService;
import com.TheJobsConsulting.service.ConsultantService;
import com.TheJobsConsulting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/V3")
public class ConsultantController {
    @Autowired
    ConsultantLoginService consultantLoginService;
    @Autowired
    ConsultantService consultantService;

    @GetMapping("/consultantDetails")
    @CrossOrigin
    public ResponseEntity<Consultant>getConsultantDetails (@RequestParam String key)throws   //returns a ResponseEntity
                                                                                       // containing a Consultant object
            LoginException, UserException{
        if (consultantLoginService.checkUserLogin(key)){                     //Check whether the consultant is logged in
            Consultant returnConsultant = consultantService.getConsultantDetails(key); //calls a method to retrieve
                                                                                       // details for the consultant
            return new ResponseEntity<Consultant>(returnConsultant, HttpStatus.CREATED);
        }else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @GetMapping("/upcomingAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getUpcomingAppointments (@RequestParam String key) throws
            LoginException, UserException, ConsultantException, AppointmentException{
        if (consultantLoginService.checkUserLogin(key)){    // Check if the user is logged in as a consultant
            CurrentSession currentUserSession = consultantService.getCurrentUserByUuid(key);
            Consultant registerConsultant = consultantService.getConsultantByUuid(key);   // Retrieve the current user session
                                                                                          // and consultant information
            if (currentUserSession == null || currentUserSession.getUserType() == null
                    || !currentUserSession.getUserType().equals("consultant")){   // Validate the user session and type
                throw new LoginException("Please Login As Consultant.");
            }
            if (registerConsultant != null){  // If the consultant is valid, retrieve and return upcoming appointments
                List<Appointment> listOfUpcomingAppointment = consultantService.getFutureAppointments(registerConsultant);
                return new ResponseEntity<List<Appointment>>(listOfUpcomingAppointment, HttpStatus.ACCEPTED);
            }else{
                throw new ConsultantException("Please Enter Valid Details.");
            }
        }else {
            throw new LoginException("Please Enter Valid Key");
        }
    }

    @GetMapping ("/pastAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getPastAppointments (@RequestParam String key) throws
            LoginException, UserException, ConsultantException, AppointmentException{
        if (consultantLoginService.checkUserLogin(key)){
            CurrentSession currentUserSession = consultantService.getCurrentUserByUuid(key);
            Consultant registerConsultant = consultantService.getConsultantByUuid(key);
            if (!currentUserSession.getUserType().equals("consultant")){
                throw new LoginException("Please Login As Consultant.");
            }
            if (registerConsultant != null){  // If the consultant is valid, retrieve and return past appointments
                List<Appointment> listOfPastAppointment = consultantService.getPastAppointments(registerConsultant);
                return new ResponseEntity<List<Appointment>>(listOfPastAppointment, HttpStatus.ACCEPTED);
            }else {
                throw new ConsultantException("Please Enter Valid Details.");
            }
        }else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }


}

