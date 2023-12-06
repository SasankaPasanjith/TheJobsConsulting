package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;

public interface ConsultantLoginService {

    LoginUUIDKey logToAccount(LoginDTO loginDTO) throws LoginException;
    String logOutAccount (String key) throws  LoginException;
    Boolean checkUserLogin (String key) throws LoginException;
}