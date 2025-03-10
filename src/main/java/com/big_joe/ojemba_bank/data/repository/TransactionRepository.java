package com.big_joe.ojemba_bank.data.repository;

import com.big_joe.ojemba_bank.data.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, String> {
}
