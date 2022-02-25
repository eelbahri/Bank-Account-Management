package com.seyle.bank.services;

public class BankAccountException extends Exception {
    public BankAccountException(String errorMessage) {
        super(errorMessage);
    }
}
