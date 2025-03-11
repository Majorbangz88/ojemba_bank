package com.big_joe.ojemba_bank.exceptions;

public class TransactionsNotFoundException extends RuntimeException {

    public TransactionsNotFoundException(String message) {
        super(message);
    }
}
