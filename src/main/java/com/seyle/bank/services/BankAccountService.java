package com.seyle.bank.services;

import com.seyle.bank.models.BankAccount;

import java.util.Collection;
import java.util.Optional;

public interface BankAccountService {

    BankAccount create(String owner, Double amount);

    Collection<BankAccount> getAll();

    Optional<BankAccount> checkAccountAmount(String id);

    BankAccount withdraw(String id, Double amount) throws BankAccountException;

    BankAccount deposit(String id, Double amount) throws BankAccountException;

    void transfer(String idPayer, String idPayee, Double amount) throws BankAccountException;
}
