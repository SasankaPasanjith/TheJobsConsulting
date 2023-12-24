package com.TheJobsConsulting.controller;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.*;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import com.TheJobsConsulting.service.ConsultantService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import com.TheJobsConsulting.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.List;

@RestController
@RequestMapping("api/V3")
public class UserController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserService userService;
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    UserAdminLoginService userAdminLoginService;
    @Autowired
    ConsultantService consultantService;

    @CrossOrigin
    @PostMapping("/registerUser")
    public ResponseEntity<User>saveUser(@Valid @RequestBody User user)throws UserException{
        User savedUser =  userService.createUser(user);                    //Calls a service method to create a new user
        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User>updateUser(@RequestBody User user, @RequestParam(required = false) String key) throws
            UserException{                      //Calls a service method to update a user with the provided data and key
        User updatedUser = userService.updateUser(user, key);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/viewUser")
    @CrossOrigin
    public ResponseEntity<User>viewUserDetails(@RequestParam String key) throws LoginException, UserException{
        if (userAdminLoginService.checkUserLogin(key)){
            User returnUser = userService.getUserDetails(key); // retrieve details about the user with the specified key
            return new ResponseEntity<User>(returnUser, HttpStatus.ACCEPTED);
        }else {
            throw new LoginException("User Not Found.");
        }
    }

    @PostMapping("/bookAppointment")
    @CrossOrigin
    public ResponseEntity<Appointment> bookAppointment (@RequestParam String key, @RequestBody Appointment appointment)
            throws LoginException, AppointmentException, ConsultantException, IOException, DateTimeException,
            MessagingException, TimeDateException {
        if (appointment == null){
            throw new AppointmentException("Please Enter Valid Appointment Details.");
        }
        if (userAdminLoginService.checkUserLogin(key)){
            Appointment registerAppointment = userService.bookAppointment(key, appointment); //Proceeds to book appointment
            return new ResponseEntity<Appointment>(registerAppointment, HttpStatus.CREATED);
        }else {
            throw new LoginException("Please Login Again.");
        }
    }

    @GetMapping("/allConsultants")
    @CrossOrigin
    public ResponseEntity<List<Consultant>> getAllConsultantsInDatabase (@RequestParam String key) throws LoginException,
            ConsultantException{
        if (userAdminLoginService.checkUserLogin(key)){
            List<Consultant> listOfConsultants = consultantService.getAllConsultantsInDb(); //Retrieve a list of consultants
                                                                                            // from the database
            return new ResponseEntity<List<Consultant>>(listOfConsultants, HttpStatus.ACCEPTED);
        }else {
            throw new LoginException("Invalid Credentials; Please Login.");
        }
    }

    @GetMapping("/getAllConsultants")
    @CrossOrigin
    public ResponseEntity<List<Consultant>> getAllConsultants(@RequestParam String key) throws
            LoginException, ConsultantException{
        if (userAdminLoginService.checkUserLogin(key)){  //Retrieves list of consultants from the userService
            List<Consultant> consultantList = userService.getAllConsultant();
            return new ResponseEntity<List<Consultant>>(consultantList, HttpStatus.ACCEPTED);
        }else {
            throw new LoginException("Invalid Credentials; Please Login.");
        }
    }


}















