package com.TheJobsConsulting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<MyErrorDetails> userExceptionHandler(UserException userException, WebRequest webRequest) {
        MyErrorDetails myErrorDetails = new MyErrorDetails();

        myErrorDetails.setDetails(webRequest.getDescription(false));
        myErrorDetails.setErrorMsg(userException.getMessage());
        myErrorDetails.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<MyErrorDetails>(myErrorDetails, HttpStatus.BAD_REQUEST);

    }

}








