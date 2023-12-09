package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.exception.UserException;

public interface ConsultantService {
    Consultant getConsultantDetails (String key)throws UserException;
}
