package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.*;

import java.util.List;

public interface ConsultantService {
    Consultant getConsultantDetails (String key)throws UserException;
    CurrentSession getCurrentUserByUuid (String uuid) throws LoginException;
    Consultant getConsultantByUuid(String uuid) throws UserException;
    List<Consultant> getAllConsultantsInDb() throws ConsultantException;
    List<Appointment> getFutureAppointments (Consultant consultant) throws AppointmentException;
    List<User> getUserList();

    Consultant forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException;

}