package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.AccountInfo;
import com.big_joe.ojemba_bank.data.model.User;
import com.big_joe.ojemba_bank.data.repository.UserRepository;
import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.UserRegRequest;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public BankResponse createAccount(UserRegRequest regRequest) {

//        User newUser = new User();
//        newUser.setFirstName(regRequest.getFirstName());
//        newUser.setOtherName(regRequest.getOtherName());
//        newUser.setLastName(regRequest.getLastName());
//        newUser.setGender(regRequest.getGender());
//        newUser.setAddress(regRequest.getAddress());
//        newUser.setStateOfOrigin(regRequest.getStateOfOrigin());
//        newUser.setAccountNumber(AccountUtil.generateAccountNumber());
//        newUser.setAccountBalance(BigDecimal.ZERO);
//        newUser.setEmail(regRequest.getEmail());
//        newUser.setPhoneNumber(regRequest.getPhoneNumber());
//        newUser.setAlternativePhoneNumber(regRequest.getAlternativePhoneNumber());
//
//        User savedUser = userRepository.save(newUser);
//
//        BankResponse response = new BankResponse();
//        response.setResponseCode(AccountUtil.ACCOUNT_CREATION_CODE);
//        response.setResponseMessage(AccountUtil.ACCOUNT_SUCCESS_MESSAGE);
//
//        AccountInfo accountInfo = new AccountInfo();
//        accountInfo.setAccountName(savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName());
//        accountInfo.setAccountNumber(savedUser.getAccountNumber());
//        accountInfo.setAccountBalance(savedUser.getAccountBalance());
//
//        response.setAccountInfo(accountInfo);
//
//        return response;

        User newUser = User.builder()
                .firstName(regRequest.getFirstName())
                .otherName(regRequest.getOtherName())
                .lastName(regRequest.getLastName())
                .gender(regRequest.getGender())
                .address(regRequest.getAddress())
                .stateOfOrigin(regRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(regRequest.getEmail())
                .phoneNumber(regRequest.getPhoneNumber())
                .alternativePhoneNumber(regRequest.getPhoneNumber())
                .build();

        User savedUser = userRepository.save(newUser);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtil.ACCOUNT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(regRequest.getFirstName() + " " + regRequest.getOtherName() + " " + regRequest.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public Long count() {
        return userRepository.count();
    }
}
