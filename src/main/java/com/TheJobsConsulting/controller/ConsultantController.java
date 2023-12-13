package com.TheJobsConsulting.controller;


import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.service.ConsultantLoginService;
import com.TheJobsConsulting.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

