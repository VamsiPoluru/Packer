package com.webapp.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.webapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findById(long id);

}
