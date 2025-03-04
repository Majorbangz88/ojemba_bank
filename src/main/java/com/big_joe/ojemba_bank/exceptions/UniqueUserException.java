package com.big_joe.ojemba_bank.exceptions;

public class UniqueUserException extends RuntimeException {

    public UniqueUserException(String message) {
        super(message);
    }
}
