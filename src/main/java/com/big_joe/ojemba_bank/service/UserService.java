package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.data.model.User;
import com.big_joe.ojemba_bank.dto.*;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void deleteAll();

    BankResponse createAccount(UserRegRequest regRequest);

    Long count();

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);

    BankResponse fundsTransfer(TransferRequest transferRequest);

    Optional<User> findByAccountNumber(String senderAccountNumber);
    List<Transactions> generateAccountStatement(TransactionEnquiryReq req) throws DocumentException, FileNotFoundException;
}
