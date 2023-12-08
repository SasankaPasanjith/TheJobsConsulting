package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.TheJobsConsulting.config.SpringDocConfig.bCryptPasswordEncoder;

@Service
public class UserServiceImplement implements UserService, Runnable {

    @Autowired
    UserDAO userDAO;
    @Autowired
    SessionDAO sessionDAO;

    @Override
    public User createUser (User user) throws UserException{
        User dbUser = userDAO.findByMobileNo(user.getMobileNo());
        if (dbUser == null){
            user.setUserType("User");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return user;
        }else{
            throw new UserException("Already Registered User by Using this Mobile Number.");
        }
    }

    @Override
    public User updateUser(User user, String key) throws UserException {
        return null;
    }

    @Override
    public List<Appointment> getUserAppointment(String key) throws AppointmentException, UserException {
        return null;
    }

    @Override
    public List<Consultant> getAllConsultant() throws ConsultantException {
        return null;
    }

    @Override
    public User getUserByUuid(String uuid) throws UserException {
        return null;
    }

    @Override
    public CurrentSession getCurrentUserByUuid(String uuid) throws LoginException {
        return null;
    }

    @Override
    public Appointment deleteAppointment(Appointment appointment) throws AppointmentException, ConsultantException, Exception {
        return null;
    }

    @Override
    public User getUserDetails(String key) throws UserException {
        return null;
    }

    @Override
    public Appointment bookAppointment(String key, Appointment appointment) throws AppointmentException, LoginException, ConsultantException, IOException, MessagingException {
        return null;
    }

    @Override
    public void run() {

    }
}
