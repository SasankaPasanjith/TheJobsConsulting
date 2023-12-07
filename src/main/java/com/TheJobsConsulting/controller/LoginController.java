package com.TheJobsConsulting.controller;

import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginResponce;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.service.ConsultantLoginService;
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
    @Autowired
    private ConsultantLoginService consultantLoginService;


    @PostMapping("/loginUser")          //Handles user/admin login requests
    @CrossOrigin
    public ResponseEntity<LoginUUIDKey> loginUser(@RequestBody LoginDTO loginDTO)throws LoginException{
        LoginUUIDKey result = userAdminLoginService.logToAccount(loginDTO);
        return new ResponseEntity<LoginUUIDKey>(result, HttpStatus.OK);
    }

    @PostMapping("/loginConsultant")     //Handles consultant login requests
    @CrossOrigin
    public ResponseEntity<LoginUUIDKey>loginConsultant(@RequestBody LoginDTO loginDTO) throws  LoginException{
        LoginUUIDKey result = consultantLoginService.logToAccount(loginDTO);
        return new ResponseEntity<LoginUUIDKey>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/checkLogin/{uuid}")  //Checks the login status of a user/admin based on the provided UUID
    public ResponseEntity<LoginResponce> checkUserLogin(@PathVariable String uuid) throws LoginException{
        Boolean loginResult = userAdminLoginService.checkUserLogin(uuid);
        LoginResponce loginResponce = new LoginResponce(loginResult);
        return new ResponseEntity<LoginResponce>(loginResponce, HttpStatus.OK);
    }

    @PostMapping("/userLogout")     //Logs out a user/admin based on the provided key
    @CrossOrigin
    public String logoutUser(@RequestParam(required = false) String key) throws LoginException{
        return userAdminLoginService.logOutAccount(key);
    }

    @PostMapping("/consultantLogout")  //Logs out a consultant based on the provided key
    @CrossOrigin
    public String logoutConsultant(@RequestParam(required = false) String key)throws LoginException{
        return consultantLoginService.logOutAccount(key);
    }
}
