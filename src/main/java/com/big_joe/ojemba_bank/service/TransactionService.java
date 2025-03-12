package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.dto.TransactionDto;
import com.big_joe.ojemba_bank.dto.TransactionEnquiryReq;
import com.big_joe.ojemba_bank.dto.TransactionResponse;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public interface TransactionService {
    void saveTransaction(TransactionDto creditTransaction);

    TransactionResponse findTransactionByReference(TransactionEnquiryReq enquiryReq);

    List<Transactions> findTransactionsByAccountNumber(TransactionEnquiryReq enquiryReq1);

    List<Transactions> allTransactions();

    List<Transactions> generateAccountStatement(TransactionEnquiryReq req) throws DocumentException, FileNotFoundException;
}
