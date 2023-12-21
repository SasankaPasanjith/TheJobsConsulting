package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.*;
import com.TheJobsConsulting.exception.*;

import java.util.List;

public interface ConsultantService {
    Consultant getConsultantDetails (String key)throws UserException;
    CurrentSession getCurrentUserByUuid (String uuid) throws LoginException;
    Consultant getConsultantByUuid(String uuid) throws UserException;
    Consultant forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException;
    List<Consultant> getAllConsultantsInDb() throws ConsultantException;
    List<Appointment> getFutureAppointments (Consultant consultant) throws AppointmentException;
    List<User> getUserList();
    List<Appointment> getPastAppointments (Consultant consultant) throws AppointmentException;
    List<Appointment> getAllAppointments (Consultant registerConsultant) throws ConsultantException;
}
