package com.TheJobsConsulting.repository;

import com.TheJobsConsulting.entity.CurrentSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionDAO extends JpaRepository<CurrentSession,Integer> {
    public CurrentSession findByUuid(String uuid);
}
