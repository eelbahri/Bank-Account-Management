package com.seyle.bank.services;

import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.BankAccount;

import java.util.Collection;
import java.util.Optional;

public interface BankAccountService {

    BankAccount create(String owner, Double amount);

    void withdraw(String id, Double amount) throws BankAccountException, InsufficientFundException;

    void deposit(String id, Double amount) throws BankAccountException;

    void transfer(String fromAccount, String toAccount, Double amount) throws Exception;

    Collection<BankAccount> getAll();

    Optional<BankAccount> checkAccountAmount(String id);

    Collection<AccountHistory> getHistory(String id) throws BankAccountException;
}
