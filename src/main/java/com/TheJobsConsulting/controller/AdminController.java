package com.TheJobsConsulting.controller;

import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.service.AdminService;
import com.TheJobsConsulting.service.UserAdminLoginService;
import com.TheJobsConsulting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.CodecException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    }


