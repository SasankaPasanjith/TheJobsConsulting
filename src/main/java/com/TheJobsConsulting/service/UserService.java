package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.*;
import jakarta.mail.MessagingException;



import java.io.IOException;
import java.util.List;
public interface UserService {
    User createUser(User customer) throws UserException;
    User updateUser(User user, String key) throws UserException;

    List<Appointment> getUserAppointment (String key) throws AppointmentException,UserException;  //provided list of
                                                                                      // appointments by the given key
    List<Consultant> getAllConsultant() throws ConsultantException; // to retrieve a list of consultants
    User getUserByUuid(String uuid) throws UserException;  //to retrieve a user by their UUID

    CurrentSession getCurrentUserByUuid(String uuid) throws LoginException;
    Appointment deleteAppointment(Appointment appointment) throws AppointmentException, ConsultantException, Exception;
    User getUserDetails(String key) throws UserException;
    Appointment bookAppointment (String key, Appointment appointment) throws AppointmentException, LoginException,
            ConsultantException, IOException, MessagingException, TimeDateException;
    User forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException;

   }
