package com.TheJobsConsulting.service;

import com.TheJobsConsulting.config.SpringDocConfig;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserAdminLoginServiceImplement implements UserAdminLoginService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    SessionDAO sessionDAO;

    @Override
    public LoginUUIDKey logToAccount(LoginDTO loginDTO) throws LoginException {
        LoginUUIDKey loginUUIDKey = new LoginUUIDKey();                        //Store information about login attempts
        User existingUser = userDAO.findByMobileNo(loginDTO.getMobileNo());

        if (existingUser == null) {
            throw new LoginException("Login Failed. Please Enter Valid Number" + loginDTO.getMobileNo());
        }
        Optional<CurrentSession> validCustomerSession = sessionDAO.findById(existingUser.getUserId());   //retrieve user
        // session by user id
        if (validCustomerSession.isPresent()) {
            if (SpringDocConfig.bCryptPasswordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
                loginUUIDKey.setUuid(validCustomerSession.get().getUuid());        //Checking the session
                loginUUIDKey.setMessage("Login Successful");                       //If the password match set session UUID
                return loginUUIDKey;
            }
            throw new LoginException("Please Enter Valid Details.");             //display error message if not
        }
        if (validCustomerSession.isPresent()) {           //Check if there is valid session already
            throw new LoginException("User is Already Logged in to the System.");
        }
        if (SpringDocConfig.bCryptPasswordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
            String key = generateRandomString();      //Generate random string key
            CurrentSession currentSession = new CurrentSession(existingUser.getUserId(), key, LocalDateTime.now());

            if (SpringDocConfig.bCryptPasswordEncoder.matches("admin", existingUser.getPassword())
                    && existingUser.getMobileNo().equals("100000")) {
                existingUser.setUserType("admin");
                currentSession.setUserType("admin");
                currentSession.setUserId(existingUser.getUserId());
                sessionDAO.save(currentSession);
                userDAO.save(existingUser);       //If the user is an admin, it sets the user type to "admin"

                loginUUIDKey.setMessage("Successfully log in as Admin");
                loginUUIDKey.setUuid(key);
                return loginUUIDKey;
            } else {

                existingUser.setUserType("user");
                currentSession.setUserId(existingUser.getUserId());
                currentSession.setUserType("user");       //If the user is an user, it sets the user type to "user"
            }
            userDAO.save(existingUser);

            sessionDAO.save(currentSession);

            loginUUIDKey.setMessage("Successfully log in  as User" + " key");
            loginUUIDKey.setUuid(key);
            return loginUUIDKey;
        } else {


            throw new LoginException("Please enter valid password");
        }
    }

    @Override
    public String logOutAccount(String key) throws LoginException {       // defines a method for logging out a user by
                                                                          // deleting their session
        CurrentSession currentUserOptional = sessionDAO.findByUuid(key);  //retrieves a CurrentSession object from a data
                                                                          // access object
        if (currentUserOptional != null) {
            sessionDAO.delete(currentUserOptional);
            return "Successfully Logout.";
        } else {
            throw new LoginException("Please Enter Valid Details.");
        }
    }

    @Override
    public Boolean checkUserLogin(String key) throws LoginException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);
        if (currentUserSession != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String generateRandomString() {
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
