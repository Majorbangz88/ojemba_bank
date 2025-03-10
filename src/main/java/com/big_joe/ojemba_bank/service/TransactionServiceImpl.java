package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.data.repository.TransactionRepository;
import com.big_joe.ojemba_bank.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        String transactionReference = generateReferenceCode();

        Transactions transaction = Transactions.builder()
                .transactionAmt(transactionDto.getTransactionAmt())
                .transactionType(transactionDto.getTransactionType())
                .senderAccountName(transactionDto.getSenderAccountName())
                .senderAccountNumber(transactionDto.getSenderAccountNumber())
                .recipientAccountName(transactionDto.getRecipientAccountName())
                .recipientAccountNumber(transactionDto.getRecipientAccountNumber())
                .transactionReference(transactionReference)
                .createdAt(LocalDateTime.now())
                .description(transactionDto.getDescription())
                .status("Successful")
                .build();

        transactionRepository.save(transaction);

        System.out.println("Transaction saved successfully");
    }

    private String generateReferenceCode() {
        StringBuilder token = new StringBuilder();
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int index = 0; index < 12; index++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }
        return token.toString();
    }
}
