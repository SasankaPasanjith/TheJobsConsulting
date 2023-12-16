package com.TheJobsConsulting.service;

import com.TheJobsConsulting.config.SpringDocConfig;
import com.TheJobsConsulting.entity.Appointment;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.User;
import com.TheJobsConsulting.exception.AppointmentException;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.repository.AppointmentDAO;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImplement implements AdminService {
    @Autowired
    ConsultantDAO consultantDAO;
    @Autowired
    AppointmentDAO appointmentDAO;
    @Autowired
    UserDAO userDAO;

    @Override
    public Consultant registerConsultant (Consultant consultant) throws ConsultantException {
        Consultant dbConsultant = consultantDAO.findByMobileNo(consultant.getMobileNo());   //check consultants with the mobile Num
        if (dbConsultant == null){
            consultant.setType("consultant");
            consultant.setPassword(SpringDocConfig.bCryptPasswordEncoder.encode(consultant.getPassword())); //password encode
            return consultantDAO.save(consultant);                         //Save the Consultant using DAO
        }else {
            throw new ConsultantException("Consultant Already Registered By Using This Number"+ consultant.getMobileNo());
        }
    }

    @Override
    public List<Consultant> getAllConsultants() throws ConsultantException {
        List<Consultant> listOfConsultant = consultantDAO.findAll();   //retrieve all consultants in DB using data access object
        if (!listOfConsultant.isEmpty()){
            return listOfConsultant;
        }
        else {
            throw new ConsultantException("There are No Consultants");
        }
    }

    @Override
    public List<User> getAllUsers() throws UserException {       //returns a list of User objects
        List<User>listUser = userDAO.findAll();
        if (!listUser.isEmpty()){
            return listUser;
        }else {
            throw new UserException("There are no any Users to Show.");
        }
    }

    @Override
    public List<Appointment> getAllAppointments() throws AppointmentException {  //returns a list of Appointment objects
        List<Appointment>listAppointment = appointmentDAO.findAll();
        if (!listAppointment.isEmpty()){
            return listAppointment;
        }else {
            throw new AppointmentException("There are no Appointments to Show.");
        }
    }

    @Override
    public List<Consultant> getAllValidInvalidConsultants(String key) throws ConsultantException {  //returns a list of Consultant objects
        List<Consultant>listConsultant = consultantDAO.findAll();
        if (!listConsultant.isEmpty()){
            return listConsultant;
        }else{
            throw new ConsultantException("There are no Consultants to Show");
        }
    }

    @Override
    public Consultant grantPermissionConsultant(Consultant consultant) throws ConsultantException {
        Optional<Consultant> registerConsultant = consultantDAO.findById(consultant.getConsultantId()); //Find a Consultant
                                                                                         // object in a DAO using its ID
        if (registerConsultant.isPresent()){
            registerConsultant.get().setValidConsultant(true);  //If the consultant is found, sets the validConsultant
                                                                // property of the found consultant to true.
            return consultantDAO.save(registerConsultant.get());  //The modified consultant is then saved back to the DB
        }else {
            throw new ConsultantException("No Consultant With This ID" +consultant.getConsultantId());
        }
    }

    @Override
    public Consultant revokePermissionConsultant(Consultant consultant) throws ConsultantException {
        Optional<Consultant> registerConsultant = consultantDAO.findById(consultant.getConsultantId());
        if (registerConsultant.isPresent()){
            registerConsultant.get().setValidConsultant(false);  //Revoking permission by marking the consultant as not valid
            return consultantDAO.save(registerConsultant.get());
        }else {
            throw new ConsultantException("No Valid Consultant With This ID"+ consultant.getConsultantId());
        }
    }

}

