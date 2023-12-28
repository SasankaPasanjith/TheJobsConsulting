package com.TheJobsConsulting.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.CodecException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/V2")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    UserAdminLoginService userAdminLoginService;

    @Autowired
    UserService userService;

    @PostMapping(path = "/registerConsultant")
    @CrossOrigin
    public ResponseEntity<Consultant> registerConsultant(@RequestParam String key, @RequestParam Consultant consultant)
            throws CodecException, LoginException, ConsultantException {
        if (userAdminLoginService.checkUserLogin(key)) {
            CurrentSession currentSession = userService.getCurrentUserByUuid(key);
            if (!currentSession.getUserType().equals("admin")) {
                throw new LoginException("You Need to Login as an Admin.");
            }
            if (consultant != null) {
                Consultant registerConsultant = adminService.registerConsultant(consultant);
                return new ResponseEntity<Consultant>(registerConsultant, HttpStatus.CREATED);
            } else {
                throw new ConsultantException("Please Enter Valid Credentials.");
            }
            }else{
                throw new LoginException("Invalid; Please Login");
            }
        }

    @GetMapping("/getAllConsultants")
    @CrossOrigin
    public ResponseEntity<List<Consultant>> getAllConsultants (@RequestParam String key) throws  //returns a ResponseEntity
                                                                             // containing a list of Consultant objects.
            LoginException, ConsultantException{
        if (userAdminLoginService.checkUserLogin(key)){
            CurrentSession currentSession = userService.getCurrentUserByUuid(key);
            if (!currentSession.getUserType().equals("admin")){
                throw new LoginException("Please Login as an Admin.");
            }
            List<Consultant> listValidConsultants = adminService.getAllValidInvalidConsultants(key); //If the user is an
                                                                        // admin it retrieves a list of valid consultants
            return new ResponseEntity<List<Consultant>>(listValidConsultants, HttpStatus.CREATED);
        }else{
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @GetMapping("/getUsers")
    @CrossOrigin
    public ResponseEntity<List<User>> getALlUsers (@RequestParam String key) throws LoginException, UserException{
        if (userAdminLoginService.checkUserLogin(key)){
            List<User> listUSer = adminService.getAllUsers();
            return  new ResponseEntity<List<User>>(listUSer, HttpStatus.ACCEPTED);
        }else {
            throw new LoginException("Error. Please Login. ");
        }
    }

    @GetMapping("/getAllAppointments")
    @CrossOrigin
    public ResponseEntity<List<Appointment>> getAllAppointment (@RequestParam String key) throws LoginException, AppointmentException{
        if (userAdminLoginService.checkUserLogin(key)){
            List<Appointment> allAppointments = adminService.getAllAppointments();
            return new ResponseEntity<List<Appointment>>(allAppointments, HttpStatus.ACCEPTED);
        }else {
            throw new LoginException("Please Login First.");
        }
    }

    @PutMapping("/revokePermission")
    @CrossOrigin
    public ResponseEntity <Consultant> revokePermissionConsultant (@RequestParam String key, @RequestBody Consultant consultant)
        throws ConsultantException, LoginException{
        if (userAdminLoginService.checkUserLogin(key)) {
            CurrentSession currentUserSession = userService.getCurrentUserByUuid(key);
            if (!currentUserSession.getUserType().equals("admin")){   //Check if the user logged in as an admin
                throw new LoginException("Please Login As An Admin.");
            }
             Consultant deletedConsultant = adminService.revokePermissionConsultant(consultant);  //Revoke permissions for the consultant
            return new ResponseEntity<Consultant>(deletedConsultant, HttpStatus.CREATED);
            }else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

    @PutMapping("/grantPermission")
    @CrossOrigin
    public ResponseEntity<Consultant> grantPermissionConsultant (@RequestParam String key, @RequestBody Consultant consultant)
        throws ConsultantException, LoginException{
        if (userAdminLoginService.checkUserLogin(key)){
            CurrentSession currentUserSession = userService.getCurrentUserByUuid(key);
            if (!currentUserSession.getUserType().equals("admin")){
                throw new LoginException("Please Login As An Admin.");
            }
            Consultant deletedConsultant = adminService.grantPermissionConsultant(consultant);
            return new ResponseEntity<Consultant>(deletedConsultant, HttpStatus.CREATED);
        }else {
            throw new LoginException("Please Enter Valid Key.");
        }
    }

}


