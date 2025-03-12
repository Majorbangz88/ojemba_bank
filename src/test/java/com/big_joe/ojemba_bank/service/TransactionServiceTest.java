package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.dto.*;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

//    @BeforeEach
//    public void setup() {
//        transactionService.deleteAll();
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

    @Test
    public void testFindTransactionByTransactionReferenceOrAccountNumber() {
        TransactionEnquiryReq enquiryReq = TransactionEnquiryReq.builder()
                .transactionReference("z3U10VJduYRy")
                .build();

        TransactionResponse transaction = transactionService.findTransactionByReference(enquiryReq);

        assertNotNull(transaction);
        assertEquals("Transaction found!", transaction.getResponseMessage());

        TransactionEnquiryReq enquiryReq1 = TransactionEnquiryReq.builder()
                .accountNumber("2025506057")
                .build();

        List<Transactions> transactions = transactionService.findTransactionsByAccountNumber(enquiryReq1);
        int numberOfTransactions = transactions.size();

        assertEquals(2, numberOfTransactions);
    }

    @Test
    public void testReturnAllTransactions() {
        List<Transactions> allTransactions = transactionService.allTransactions();
        assertEquals(5, allTransactions.size());
    }

    @Test
    public void generateAccountStatement() throws DocumentException, FileNotFoundException {
        TransactionEnquiryReq req = TransactionEnquiryReq.builder()
                .accountNumber("2025841506")
                .startDate("01/03/2025")
                .endDate("11/03/2025")
                .build();

        List<Transactions> transactions = transactionService.generateAccountStatement(req);

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty(), "No transactions found for the given period");
        System.out.println("Transactions count: " + transactions.size());
    }
}
