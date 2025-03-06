package com.big_joe.ojemba_bank.utils;

import java.time.Year;

public class AccountUtil {

    public static final String ACCOUNT_CREATION_CODE = "001";
    public static final String ACCOUNT_SUCCESS_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This Account Exists Already!";
    public static final String ACCOUNT_EXISTS_CODE = "002";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "This Account does not exist!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account Found!";
    public static final String ACCOUNT_CREDIT_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Credit Transaction Successful";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Your Account Balance Is Insufficient For the transaction you to carryout";
    public static final String ACCOUNT_DEBIT_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "Debit Transaction Successful";
    public static final String INVALID_AMOUNT_CODE = "007";
    public static final String INVALID_AMOUNT_MESSAGE = "The amount you entered is invalid. Please enter a valid amount";

    public static String generateAccountNumber() {
        int min = 100000;
        int max = 999999;

        Year year = Year.now();

        int randomNum = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String currentYear = String.valueOf(year);
        String randomNumber = String.valueOf(randomNum);

        return currentYear + randomNumber;

    }
}
