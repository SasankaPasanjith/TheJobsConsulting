package com.TheJobsConsulting.service;

import com.TheJobsConsulting.entity.Email;
import jakarta.mail.MessagingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplement implements EmailService {
    private JavaMailSender javaMailSender;

    public EmailServiceImplement(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    @Override
    public Boolean sendAppointmentBookingMail(String toEmail, Email emailBody) throws MessagingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("sasanka.pasanjith@yandex.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(emailBody.getEmailBody());
        simpleMailMessage.setSubject(emailBody.getEmailSubject());
        javaMailSender.send(simpleMailMessage);
        return true;
    }
}


