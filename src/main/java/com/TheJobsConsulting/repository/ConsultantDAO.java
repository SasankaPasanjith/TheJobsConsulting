package com.TheJobsConsulting.repository;

import com.TheJobsConsulting.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultantDAO extends JpaRepository<Consultant, Integer> {
    public Consultant findByMobileNo(String mobileNo);
}
