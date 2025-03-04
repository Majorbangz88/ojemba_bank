package com.big_joe.ojemba_bank.utils;

import java.time.Year;

public class AccountUtil {

    public static final String ACCOUNT_CREATION_CODE = "001";
    public static final String ACCOUNT_SUCCESS_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This Account Exists Already!";
    public static final String ACCOUNT_EXISTS_CODE = "002";

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
