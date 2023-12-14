package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;

import java.util.List;

public interface ConsultantService {
    Consultant getConsultantDetails (String key)throws UserException;
    CurrentSession getCurrentUserByUuid (String uuid) throws LoginException;
    Consultant getConsultantByUuid(String uuid) throws UserException;
    List<Consultant> getAllConsultantsInDb() throws ConsultantException;
    List<Appointment> getFutureAppointments (Consultant consultant) throws AppointmentException;
    List<User> getUserList();

}