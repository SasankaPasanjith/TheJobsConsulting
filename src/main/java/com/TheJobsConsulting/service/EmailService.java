package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Email;
import jakarta.mail.MessagingException;

public interface EmailService {
    Boolean sendAppointmentBookingMail (String toEmail, Email email) throws MessagingException;
}
