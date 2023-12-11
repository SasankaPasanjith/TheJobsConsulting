package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.SessionDAO;
import com.TheJobsConsulting.repository.UserDAO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.TheJobsConsulting.config.SpringDocConfig.bCryptPasswordEncoder;

@Service
public class UserServiceImplement implements UserService, Runnable {

    @Autowired
    UserDAO userDAO;
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    ConsultantDAO consultantDAO;

    @Override
    public User createUser (User user) throws UserException{
        User dbUser = userDAO.findByMobileNo(user.getMobileNo());  //Check is there any user with the given mobile number
        if (dbUser == null){
            user.setUserType("User");                             //Set user type to user
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));   //encrypt the password
            userDAO.save(user);                               //save the user
            return user;
        }else{
            throw new UserException("Already Registered User by Using this Mobile Number.");
        }
    }

    @Override
    public User updateUser(User user, String key) throws UserException {
        CurrentSession loginUser = sessionDAO.findByUuid(key);   // Retrieves object from the data access layer
        if (loginUser ==null){
            throw new UserException("Please Provide the Valid Details to Update the User");
        }
        if (user.getUserId() == loginUser.getUserId()){
            return userDAO.save(user);
        }else {
            throw new UserException("Invalid Details. Please Login Again.");
        }
    }

    @Override
    public User getUserByUuid(String uuid) throws UserException {
       CurrentSession currentUser = sessionDAO.findByUuid(uuid);  // Retrieve the current session based on the provided UUID
        Optional<User>user = userDAO.findById(currentUser.getUserId());  // Retrieve the user based on the user ID from
                                                                         // the current session
        if (user.isPresent()){                                         // Check if the user is present in the database
            return user.get();
        }else {
            throw new UserException("No User Found on this Key"+uuid);
        }
    }

    @Override
    public User getUserDetails(String key) throws UserException {
        CurrentSession currentUserSession = sessionDAO.findByUuid(key);   // Retrieve the current session based on the
                                                                          // provided key
        Optional<User>registeredUser = userDAO.findById(currentUserSession.getUserId());  // Retrieve the registered user
                                                                        // based on the user ID from the current session
        if (registeredUser.isPresent()){
            return registeredUser.get();
        }else {
            throw new UserException("No User Found. Please Try Again.");
        }
    }

    @Override
    public CurrentSession getCurrentUserByUuid(String uuid) throws LoginException {  //find a user session based on the provided UUID
        CurrentSession currentUserSession = sessionDAO.findByUuid(uuid);
        if (currentUserSession != null){
            return currentUserSession;
        }else {
            throw new LoginException("No User Found From This Key.");
        }
    }

    @Override
    public List<Consultant> getAllConsultant() throws ConsultantException {    //retrieve all consultants from the database
        List<Consultant>listOfConsultant = consultantDAO.findAll();
        if (!listOfConsultant.isEmpty()){
            listOfConsultant = listOfConsultant.stream().collect(Collectors.toList()); //If there are consultants, the list
                                                                                       // is converted to a new list and returned
            return listOfConsultant;
        }else {
            throw new ConsultantException("No Registered Consultants.");
        }
    }


    @Override
    public List<Appointment> getUserAppointment(String key) throws AppointmentException, UserException {
        return null;
    }

    @Override
    public Appointment deleteAppointment(Appointment appointment) throws AppointmentException, ConsultantException, Exception {
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
