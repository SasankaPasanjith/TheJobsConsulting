package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Consultant;
import com.TheJobsConsulting.entity.ForgotPassword;
import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;
import com.TheJobsConsulting.exception.PasswordException;

public interface ConsultantLoginService {

    LoginUUIDKey logToAccount(LoginDTO loginDTO) throws LoginException;
    String logOutAccount (String key) throws  LoginException;
    Boolean checkUserLogin (String key) throws LoginException;

    Consultant forgotPassword(String key, ForgotPassword forgotPassword) throws PasswordException;
}

