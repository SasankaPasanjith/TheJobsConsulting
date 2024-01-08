package com.TheJobsConsulting;

import com.TheJobsConsulting.controller.UserController;
import com.TheJobsConsulting.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


}
