package com.TheJobsConsulting.controller;

import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import com.TheJobsConsulting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/V3")
public class UserController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserService userService;
    @Autowired
    SessionDAO sessionDAO;

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
}













