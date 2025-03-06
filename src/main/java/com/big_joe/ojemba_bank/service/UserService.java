package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.*;

public interface UserService {
    void deleteAll();

    BankResponse createAccount(UserRegRequest regRequest);

    Long count();

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);

    BankResponse fundsTransfer(TransferRequest transferRequest);
}
