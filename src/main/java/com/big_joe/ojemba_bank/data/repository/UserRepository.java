package com.big_joe.ojemba_bank.data.repository;

import com.big_joe.ojemba_bank.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
