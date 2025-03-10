package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.CreditDebitRequest;
import com.big_joe.ojemba_bank.dto.TransactionDto;
import com.big_joe.ojemba_bank.dto.TransferRequest;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

//    @BeforeEach
//    public void setup() {
//        transactionService.deleteAll
//    }

    @Autowired
    UserService userService;

    @Test
    public void testSaveTransactionDetails() {
        CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                .accountNumber("2025841506")
                .amount(BigDecimal.valueOf(50000))
                .build();

        BankResponse creditResponse = userService.creditAccount(creditRequest);

        assertNotNull(creditResponse);
        assertEquals(0, BigDecimal.valueOf(250000).compareTo(creditResponse.getAccountInfo().getAccountBalance()));

        TransferRequest transferRequest = TransferRequest.builder()
                .sourceAccount("2025841506")
                .destinationAccount("2025237590")
                .transferAmount(BigDecimal.valueOf(50000))
                .build();

        BankResponse transferResponse = userService.fundsTransfer(transferRequest);
        assertNotNull(transferResponse);

        assertEquals(0, BigDecimal.valueOf(200000).compareTo(transferResponse.getAccountInfo().getAccountBalance()));

        TransactionDto creditTransaction = TransactionDto.builder()
                .transactionAmt(BigDecimal.valueOf(50000))
                .transactionType("Transfer")
                .senderAccountName("Joel Chimaobi Chukwu")
                .senderAccountNumber("2025841506")
                .recipientAccountName("Nnamdi Solomon Chukwu")
                .recipientAccountNumber("2025237590")
                .description("Payment for Honda engine")
                .build();

        transactionService.saveTransaction(creditTransaction);
    }
}
