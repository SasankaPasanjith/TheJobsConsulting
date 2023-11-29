package com.TheJobsConsulting.service;

import com.TheJobsConsulting.config.SpringDocConfig;
import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.repository.AppointmentDAO;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

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
        List<Consultant> listOfConsultant = consultantDAO.findAll();   //retrieve all consultants id DB using DAO
        if (!listOfConsultant.isEmpty()){
            return listOfConsultant;
        }
        else {
            throw new ConsultantException("There are No Consultants");
        }
    }
}

