package com.seyle.bank.services;

public class InsufficientFundException extends Exception {

    public InsufficientFundException(String message) {
        super(message);
    }
}
