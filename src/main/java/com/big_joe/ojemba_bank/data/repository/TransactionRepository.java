package com.big_joe.ojemba_bank.data.repository;

import com.big_joe.ojemba_bank.data.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transactions, String> {
    Optional<Transactions> findByTransactionReference(String transactionReference);

    List<Transactions> findBySenderAccountNumber(String accountNumber);
}
