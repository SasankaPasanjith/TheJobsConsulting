package com.TheJobsConsulting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "id_table")       //To Generate Unique Primary Key

    @SequenceGenerator(
            name = "id_table",
            sequenceName = "id_sequence",
            allocationSize = 1)         //To Maintain Uniqueness of the Primary Key

    private Integer userId;
    private String userName;
    @Pattern(regexp = "^[0-9]{10}$", message = "Please Enter Valid Mobile Number.") //mobile Num Validation
    private String mobileNo;
    private String password;
    @Email(message = "Please Enter Valid Email Address.")  //Email Validation
    private String email;
    private String userType;

     @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
     List<Appointment> listOfAppointments = new ArrayList<>();
}
