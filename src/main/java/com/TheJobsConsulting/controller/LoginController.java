package com.TheJobsConsulting.controller;

import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.service.UserAdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/V1")
public class LoginController {
    @Autowired
    private UserAdminLoginService userAdminLoginService;


    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<LoginUUIDKey> loginUser(@RequestBody LoginDTO loginDTO)throws LoginException{
        LoginUUIDKey result = userAdminLoginService.logToAccount(loginDTO);
        return new ResponseEntity<LoginUUIDKey>(result, HttpStatus.OK);
    }
}
