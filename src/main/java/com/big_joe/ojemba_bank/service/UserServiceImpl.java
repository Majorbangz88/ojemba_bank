package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.data.model.AccountInfo;
import com.big_joe.ojemba_bank.data.model.User;
import com.big_joe.ojemba_bank.data.repository.UserRepository;
import com.big_joe.ojemba_bank.dto.*;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public BankResponse createAccount(UserRegRequest regRequest) {
        Optional<User> foundUser = userRepository.findByEmail(regRequest.getEmail());

        if (foundUser.isPresent()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

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

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome To Ojemba Bank Plc.")
                .messageBody("Hello " + savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName() + ",\n" +
                        "We want to specially thank you for choosing Ojemba. Indeed, we stand true to our name as we promise you a jolly ride. \n" +
                        "Kindly find your account details below: \n" +
                        "Account Number: " + savedUser.getAccountNumber() + ",\n" +
                        "Account Balance: NGN" + savedUser.getAccountBalance() + ".00\n" +
                        "PLEASE FEEL FREE TO REACH OUT TO US FOR ANY ENQUIRIES THROUGH ANY OF OUR CHANNELS.\n" +
                        "\n Kind regards,\n" +
                        "Chukwu Joel Chimaobi\n" +
                        "Head, Customer Success Dept!")
                .build();

        emailService.sendEmailAlert(emailDetails);

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        Optional<User> foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.get().getAccountBalance())
                        .accountNumber(foundUser.get().getAccountNumber())
                        .accountName(foundUser.get().getFirstName() + " " + foundUser.get().getOtherName() + " " + foundUser.get().getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Optional<User> foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.map(user -> user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName()).orElse(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        Optional<User> foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeCredited = foundUser.get();

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INVALID_AMOUNT_CODE)
                    .responseMessage(AccountUtil.INVALID_AMOUNT_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        userToBeCredited.setAccountBalance(userToBeCredited.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToBeCredited);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREDIT_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToBeCredited.getFirstName() + " " + userToBeCredited.getOtherName() + " " + userToBeCredited)
                        .accountNumber(userToBeCredited.getAccountNumber())
                        .accountBalance(userToBeCredited.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        Optional<User> foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        if (foundUser.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToBeDebited = foundUser.get();

        if (validateAmount(request, userToBeDebited)) return BankResponse.builder()
                .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                .accountInfo(null)
                .build();

        userToBeDebited.setAccountBalance(userToBeDebited.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToBeDebited);

        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToBeDebited.getFirstName() + " " + userToBeDebited.getOtherName() + " " + userToBeDebited.getLastName())
                        .accountNumber(userToBeDebited.getAccountNumber())
                        .accountBalance(userToBeDebited.getAccountBalance())
                        .build())
                .build();
    }

    private static boolean validateAmount(CreditDebitRequest request, User userToBeDebited) {
        return request.getAmount().compareTo(userToBeDebited.getAccountBalance()) > 0;
    }

    @Override
    public BankResponse fundsTransfer(TransferRequest transferRequest) {
        Optional<User> sender = userRepository.findByAccountNumber(transferRequest.getSourceAccount());
        Optional<User> receiver = userRepository.findByAccountNumber(transferRequest.getDestinationAccount());

        if (sender.isEmpty() || receiver.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccount = sender.get();
        User recipientAccount = receiver.get();

        if (sourceAccount.getAccountBalance().compareTo(transferRequest.getTransferAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getTransferAmount()));
        userRepository.save(sourceAccount);

        recipientAccount.setAccountBalance(recipientAccount.getAccountBalance().add(transferRequest.getTransferAmount()));
        userRepository.save(recipientAccount);

        User updatedSender = userRepository.findByAccountNumber(sourceAccount.getAccountNumber()).get();


        return BankResponse.builder()
                .responseCode(AccountUtil.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtil.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(updatedSender.getFirstName() + " " + sourceAccount.getOtherName() + " " + sourceAccount.getLastName())
                        .accountNumber(updatedSender.getAccountNumber())
                        .accountBalance(updatedSender.getAccountBalance())
                        .build())
                .build();
    }
}
