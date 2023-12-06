package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.UserException;

import java.util.List;

public interface AdminService {
    Consultant registerConsultant(Consultant consultant) throws ConsultantException; //register consultant
    List <Consultant> getAllConsultants() throws ConsultantException;  //retrieve list of all consultant

    Consultant revokePermissionConsultant (Consultant consultant) throws ConsultantException;  //consultant revoke permission

    Consultant grantPermissionConsultant (Consultant consultant) throws ConsultantException; //consultant grant permission

    List<User> getAllUsers()throws UserException;   //retrieve list of all Users
    List<Appointment> getAllAppointments()throws AppointmentException; //retrieve list of all Appointments
    List<Consultant> getAllValidInvalidConsultants(String key) throws ConsultantException; //retrieve list of all valid
                                                                                           // & invalid Consultants
}



