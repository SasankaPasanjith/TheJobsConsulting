package com.TheJobsConsulting.controller;


import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.ForgotPassword;
import com.TheJobsConsulting.exception.*;
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
@RequestMapping("api/V4")
public class ConsultantController {
    @Autowired
    ConsultantLoginService consultantLoginService;
    @Autowired
    ConsultantService consultantService;

    @GetMapping("/consultantDetails")
    @CrossOrigin
    public ResponseEntity<Consultant> getConsultantDetails(@RequestParam String key) throws   //returns a ResponseEntity
            // containing a Consultant object
            LoginException, UserException {
        if (consultantLoginService.checkUserLogin(key)) {                     //Check whether the consultant is logged in
            Consultant returnConsultant = consultantService.getConsultantDetails(key); //calls a method to retrieve
            // details for the consultant
            return new ResponseEntity<Consultant>(returnConsultant, HttpStatus.CREATED);
        } else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @GetMapping("/upcomingAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@RequestParam String key) throws
            LoginException, UserException, ConsultantException, AppointmentException {
        if (consultantLoginService.checkUserLogin(key)) {    // Check if the user is logged in as a consultant
            CurrentSession currentUserSession = consultantService.getCurrentUserByUuid(key);
            Consultant registerConsultant = consultantService.getConsultantByUuid(key);   // Retrieve the current user session
            // and consultant information
            if (currentUserSession == null || currentUserSession.getUserType() == null
                    || !currentUserSession.getUserType().equals("consultant")) {   // Validate the user session and type
                throw new LoginException("Please Login As Consultant.");
            }
            if (registerConsultant != null) {  // If the consultant is valid, retrieve and return upcoming appointments
                List<Appointment> listOfUpcomingAppointment = consultantService.getFutureAppointments(registerConsultant);
                return new ResponseEntity<List<Appointment>>(listOfUpcomingAppointment, HttpStatus.ACCEPTED);
            } else {
                throw new ConsultantException("Please Enter Valid Details.");
            }
        } else {
            throw new LoginException("Please Enter Valid Key");
        }
    }

    @GetMapping("/pastAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getPastAppointments(@RequestParam String key) throws
            LoginException, UserException, ConsultantException, AppointmentException {
        if (consultantLoginService.checkUserLogin(key)) {
            CurrentSession currentUserSession = consultantService.getCurrentUserByUuid(key);
            Consultant registerConsultant = consultantService.getConsultantByUuid(key);
            if (!currentUserSession.getUserType().equals("consultant")) {
                throw new LoginException("Please Login As Consultant.");
            }
            if (registerConsultant != null) {  // If the consultant is valid, retrieve and return past appointments
                List<Appointment> listOfPastAppointment = consultantService.getPastAppointments(registerConsultant);
                return new ResponseEntity<List<Appointment>>(listOfPastAppointment, HttpStatus.ACCEPTED);
            } else {
                throw new ConsultantException("Please Enter Valid Details.");
            }
        } else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @GetMapping("/getAllAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getAllAppointments(@RequestParam String key) throws LoginException,
            UserException, AppointmentException, ConsultantException {
        if (consultantLoginService.checkUserLogin(key)) {
            CurrentSession currentUserSession = consultantService.getCurrentUserByUuid(key);
            Consultant registerConsultant = consultantService.getConsultantByUuid(key);
            if (!currentUserSession.getUserType().equals("consultant")) {
                throw new LoginException("Please Login As Consultant.");
            }
            if (registerConsultant != null) {
                List<Appointment> listOfPastAppointment = consultantService.getAllAppointments(registerConsultant);   //If the consultant
                // is valid, it retrieves a list of past appointments for that consultant
                return new ResponseEntity<List<Appointment>>(listOfPastAppointment, HttpStatus.ACCEPTED);
            } else {
                throw new ConsultantException("Please Enter Valid Details.");
            }

        } else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @PutMapping("/forgotPassword")
    @CrossOrigin
    public ResponseEntity<Consultant> forgotPassword(@RequestParam String key, @RequestBody ForgotPassword forgotPassword)
            throws LoginException, PasswordException {
        if (consultantLoginService.checkUserLogin(key)) {
            if (forgotPassword.getNewPassword().equals(forgotPassword.getConfirmNewPassword())) {  //Proceeds to check if
                                                                 // the new password and the confirm new password match
                if (forgotPassword.getCurrentPassword().equals(forgotPassword.getNewPassword())) { //If the passwords match,
                                                            // checks if the current password is the same as the new password.
                    throw new PasswordException("Please Enter New Password.");
                }
                Consultant finalResult = consultantService.forgotPassword(key, forgotPassword);
                return new ResponseEntity<Consultant>(finalResult, HttpStatus.ACCEPTED);
            } else {
                throw new PasswordException("Confirmed New Password Does Not Match. Please Re Enter.");
            }
        } else {
            throw new LoginException("Please Login.");
        }
    }

}




