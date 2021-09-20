package com.seyle.bank.services;

import com.seyle.bank.models.BankAccount;

import java.util.Collection;
import java.util.Optional;

public interface BankAccountService {

    void create(BankAccount bankAccount);
    Optional<BankAccount> checkAccountAmount(String id);
    void withdraw(String id, Double amount);
    void deposit(String id, Double amount);
    Collection<BankAccount> getAll();
}
