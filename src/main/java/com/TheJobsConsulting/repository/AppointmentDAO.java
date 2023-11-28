package com.TheJobsConsulting.repository;

import com.TheJobsConsulting.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentDAO extends JpaRepository<Appointment, Integer> {

}
