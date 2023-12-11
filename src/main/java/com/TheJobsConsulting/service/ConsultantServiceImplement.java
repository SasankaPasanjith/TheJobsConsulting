package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.CurrentSession;
import com.TheJobsConsulting.exception.ConsultantException;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.UserException;
import com.TheJobsConsulting.repository.ConsultantDAO;
import com.TheJobsConsulting.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultantServiceImplement implements ConsultantService {
    @Autowired
    ConsultantDAO consultantDAO;
    @Autowired
    SessionDAO sessionDAO;

    @Override
    public Consultant getConsultantDetails(String key) throws UserException {
        CurrentSession currentConsultant = sessionDAO.findByUuid(key);  //Retrieve the current session for the
                                                                        // consultant based on the provided key
        Optional<Consultant> consultant = consultantDAO.findById(currentConsultant.getUserId());   // consultant based on
                                                                                  // the user ID from the current session
        if (consultant.isPresent()){
            return consultant.get();
        }else {
            throw new UserException("There is No Consultant From this Key"+key);
        }
    }

    @Override
    public CurrentSession getCurrentUserByUuid(String uuid) throws LoginException { //find a user session based on the
                                                                                    // provided UUID
        CurrentSession currentUserSession = sessionDAO.findByUuid(uuid);
        if (currentUserSession != null) {
            return currentUserSession;
        }else {
            throw new LoginException ("Please Enter Valid Key.");
        }
    }

    @Override
    public Consultant getConsultantByUuid(String uuid) throws UserException {    //find a consultant session based on the
                                                                                 // provided UUID
        CurrentSession currentConsultant = sessionDAO.findByUuid(uuid);
        Optional<Consultant> consultant = consultantDAO.findById(currentConsultant.getUserId());  //session to find
                                                                                                  // a consultant by ID
        if (consultant.isPresent()){
            return consultant.get();
        }else {
            throw new UserException("No Consultants From This Key."+ uuid);
        }
    }

    @Override
    public List<Consultant> getAllConsultantsInDb() throws ConsultantException {   //retrieve all consultants from the database
        List<Consultant> listConsultant = consultantDAO.findAll();
        if (listConsultant.isEmpty()){
            throw new ConsultantException("No Registered Consultants.");
        }else {
            return listConsultant;
        }
    }

}
