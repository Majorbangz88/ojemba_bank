package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto creditTransaction);
}
