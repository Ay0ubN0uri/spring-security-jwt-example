package com.a00n.springsecurityjwtexample.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a00n.springsecurityjwtexample.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

}
