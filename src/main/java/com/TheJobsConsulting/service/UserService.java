package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.UserException;

import java.util.List;

public interface UserService {
    User createUser(User customer) throws UserException;
    User updateUser(User user, String key) throws UserException;

    List<Appointment> getUserAppointment (String key) throws AppointmentException,UserException;
    List<Consultant> getAllConsultant() throws ConsultantException;

}