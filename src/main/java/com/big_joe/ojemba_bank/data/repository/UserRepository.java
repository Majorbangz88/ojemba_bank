package com.big_joe.ojemba_bank.data.repository;

import com.big_joe.ojemba_bank.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByAccountNumber(String accountNumber);
}
