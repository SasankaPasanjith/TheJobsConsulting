package com.TheJobsConsulting.service;

import com.TheJobsConsulting.config.SpringDocConfig;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

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
    public String logOutAccount(String key) throws LoginException {
        return null;
    }

    @Override
    public Boolean checkUserLogin(String key) throws LoginException {
        return null;
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

