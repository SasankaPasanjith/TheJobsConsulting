package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.exception.ConsultantException;

import java.util.List;

public interface AdminService {
    Consultant registerConsultant(Consultant consultant) throws ConsultantException; //register consultant
    List <Consultant> getAllConsultants() throws ConsultantException;  //retrieve list of all consultant
}
