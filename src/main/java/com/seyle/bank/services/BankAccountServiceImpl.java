package com.seyle.bank.services;

import com.seyle.bank.models.BankAccount;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class BankAccountServiceImpl implements BankAccountService {

    private Collection<BankAccount> cache;

    @Override
    public void create(BankAccount bankAccount) {

    }

    @Override
    public Optional<BankAccount> checkAccountAmount(String id) {
        // return this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        return Optional.empty();
    }

    @Override
    public void withdraw(String id, Double amount) {

    }

    @Override
    public void deposit(String id, Double amount) {

    }

    @Override
    public Collection<BankAccount> getAll() {
        System.out.println("Get all bank accounts");
        return this.cache;
    }
}
