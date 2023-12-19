package com.TheJobsConsulting.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Email {
    String emailSubject;
    String emailBody;
}
