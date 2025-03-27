package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.data.repository.TransactionRepository;
import com.big_joe.ojemba_bank.dto.*;
import com.big_joe.ojemba_bank.exceptions.TransactionsNotFoundException;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    EmailService emailService;

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

    @Override
    public TransactionResponse findTransactionByReference(TransactionEnquiryReq enquiryReq) {
        Optional<Transactions> transaction = transactionRepository.findByTransactionReference(enquiryReq.getTransactionReference());

        if (transaction.isEmpty()) {
            return TransactionResponse.builder()
                    .responseCode(AccountUtil.TRANSACTION_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.TRANSACTION_NOT_EXIST_MESSAGE)
                    .transaction(null)
                    .build();
        }

        Transactions foundTransaction = transaction.get();

        return TransactionResponse.builder()
                .responseCode(AccountUtil.TRANSACTION_EXIST_CODE)
                .responseMessage(AccountUtil.TRANSACTION_EXIST_MESSAGE)
                .transaction(Transactions.builder()
                        .transactionType(foundTransaction.getTransactionType())
                        .senderAccountName(foundTransaction.getSenderAccountName())
                        .senderAccountNumber(foundTransaction.getSenderAccountNumber())
                        .recipientAccountName(foundTransaction.getRecipientAccountName())
                        .recipientAccountNumber(foundTransaction.getRecipientAccountNumber())
                        .transactionAmt(foundTransaction.getTransactionAmt())
                        .description(foundTransaction.getDescription())
                        .build())
                .build();
    }

    @Override
    public List<Transactions> findTransactionsByAccountNumber(TransactionEnquiryReq enquiryReq1) {
        List<Transactions> transaction = transactionRepository.findBySenderAccountNumber(enquiryReq1.getAccountNumber());

        if (transaction.isEmpty()) {
            throw new TransactionsNotFoundException("User with this Account number does not exist, hence tansactions not found!");
        }
        return transaction;
    }

    @Override
    public List<Transactions> allTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transactions> findAll() {
        return transactionRepository.findAll();
    }

}
