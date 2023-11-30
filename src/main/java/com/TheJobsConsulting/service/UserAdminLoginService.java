package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.LoginDTO;
import com.TheJobsConsulting.entity.LoginUUIDKey;
import com.TheJobsConsulting.exception.LoginException;

public interface UserAdminLoginService {
    LoginUUIDKey logToAccount(LoginDTO loginDTO) throws LoginException;
    String logOutAccount(String key) throws LoginException;
    Boolean checkUserLogin(String key) throws LoginException;  //Check whether the user is login to the system or not
}