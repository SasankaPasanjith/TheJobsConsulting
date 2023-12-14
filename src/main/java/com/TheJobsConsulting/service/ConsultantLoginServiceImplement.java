package com.TheJobsConsulting.service;

import com.TheJobsConsulting.config.SpringDocConfig;
import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.PasswordException;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.TheJobsConsulting.config.SpringDocConfig.bCryptPasswordEncoder;

@Service
public class ConsultantLoginServiceImplement  implements ConsultantLoginService {
    @Autowired
    ConsultantDAO consultantDAO;
    @Autowired
    SessionDAO sessionDAO;

    @Override
    public LoginUUIDKey logToAccount(LoginDTO loginDTO) throws LoginException {
        LoginUUIDKey loginUUIDKey = new LoginUUIDKey();                        //Store information about login attempts

        Consultant existingConsultant = consultantDAO.findByMobileNo(loginDTO.getMobileNo());

        if (existingConsultant == null) {
            throw new LoginException(("Please Enter Valid Number." + loginDTO.getMobileNo()));
        }
        Optional<CurrentSession> validCustomerSession = sessionDAO.findById(existingConsultant.getConsultantId());
                                                                                             //retrieve Consultant
        if (validCustomerSession.isPresent()) {
            throw new LoginException("Consultant Already Logged in.");
        }
        if (SpringDocConfig.bCryptPasswordEncoder.matches(loginDTO.getPassword(), existingConsultant.getPassword())) {
            String key = genarateRandomString();                                            //Generate random string key

            CurrentSession currentUserSession = new CurrentSession(existingConsultant.getConsultantId(),
                    key, LocalDateTime.now());                                             //creates a new Session object

            existingConsultant.setType("Consultant");
            currentUserSession.setUserId(existingConsultant.getConsultantId());
            currentUserSession.setUserType("Consultant");
            consultantDAO.save(existingConsultant);
            sessionDAO.save(currentUserSession);
            loginUUIDKey.setMessage("Successfully log in  as Consultant" + " key");
            loginUUIDKey.setUuid(key);

            return loginUUIDKey;
        } else {
            throw new LoginException("Please Enter Valid Credentials.");
        }
    }


    @Override
    public String logOutAccount(String key) throws LoginException {    // defines a method for logging out a Consultant by
                                                                       // deleting their session
        CurrentSession currentConsultantOptional = sessionDAO.findByUuid(key);   //retrieves a CurrentSession object
                                                                                 // from a data access object
        if (currentConsultantOptional != null){
            sessionDAO.delete(currentConsultantOptional);
            return "Successfully Logged Out.";
        }else {
            throw new LoginException("Please Enter Valid Credentials.");
        }
    }

    @Override
    public Boolean checkUserLogin(String key) throws LoginException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);       //Checking is there any existing login
        if (currentUserSession != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Consultant forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);
        Optional<Consultant> existingConsultant = consultantDAO.findById(currentUserSession.getUserId());  //Retrieves a consultant by calling
                                                                                        // the findById method on a consultantDAO object
        Boolean passwordMatch = bCryptPasswordEncoder.matches
                (forgotPassword.getCurrentPassword(), existingConsultant.get().getPassword());   //Checks the current password
                                   // provided in the ForgotPassword object matches the stored password of the consultant obtained from the DB
        if (passwordMatch){
            existingConsultant.get().setPassword(bCryptPasswordEncoder.encode(forgotPassword.getNewPassword()));  //sets the consultant
                                                                       // password to the hashed version of the new password
            return consultantDAO.save(existingConsultant.get());      //saves the updated consultant object (with the new password) to the DB
        }else {
            throw new PasswordException("Error. Password Does Not Match.");
        }
    }

    public static String genarateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();            //used to efficiently build and manipulate strings
        Random random = new Random();                        //used to generate random numbers
        while (salt.length() < 18) {                         // length of the random string (less than 18)
            int index = (int) (random.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}

