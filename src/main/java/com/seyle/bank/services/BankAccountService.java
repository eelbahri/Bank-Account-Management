package com.seyle.bank.services;

import com.seyle.bank.models.BankAccount;

import java.util.Collection;
import java.util.Optional;

public interface BankAccountService {

    void create(String owner, Double amount);
    Optional<BankAccount> checkAccountAmount(String id);
    void withdraw(String id, Double amount) throws Exception;
    void deposit(String id, Double amount) throws Exception;
    Collection<BankAccount> getAll();
}
