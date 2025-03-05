package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.EnquiryRequest;
import com.big_joe.ojemba_bank.dto.UserRegRequest;

public interface UserService {
    void deleteAll();

    BankResponse createAccount(UserRegRequest regRequest);

    Long count();

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
}
