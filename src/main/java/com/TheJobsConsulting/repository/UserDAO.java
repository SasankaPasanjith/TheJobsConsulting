package com.TheJobsConsulting.repository;

import com.TheJobsConsulting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    public User findByMobileNo(String mobileNo);
}
