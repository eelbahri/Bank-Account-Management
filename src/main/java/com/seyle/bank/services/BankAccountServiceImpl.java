package com.seyle.bank.services;

import com.google.common.collect.Lists;
import com.seyle.bank.models.BankAccount;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private Collection<BankAccount> cache = Lists.newArrayList();

    @Override
    public void create(String owner, Double amount) {
        BankAccount newAccount = BankAccount.builder()
            .owner(owner)
            .amount(amount)
            .build();
        log.info("New Account added in cache : ", newAccount.toString());
        this.cache.add(newAccount);
    }

    @Override
    public Optional<BankAccount> checkAccountAmount(String id) {
        log.info("Looking for account {}", id);
        return this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public void withdraw(String id, Double amount) throws Exception {
        Optional<BankAccount> optAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        if (optAccount.isPresent()) {
            log.info("Withdraw of {} for Bank account {}", amount, id);
            BankAccount bankAccount = optAccount.get();
            if (bankAccount.getAmount() > amount) {
                throw new Exception("There is not enough on this account, please make a deposit");
            }
            bankAccount.withdraw(amount);
            log.info("Account {} has now {}", id, bankAccount.getAmount());
        } else {
            log.error("Bank account with id {} has not been found", id);
            throw new Exception("Bank account has not been found");
        }
    }

    @Override
    public void deposit(String id, Double amount) throws Exception {
        Optional<BankAccount> optAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        if (optAccount.isPresent()) {
            log.info("Deposit of {} for account {}", amount, id);
            BankAccount bankAccount = optAccount.get();
            bankAccount.deposit(amount);
            log.info("Account {} has now {}", id, bankAccount.getAmount());
        } else {
            log.error("Bank account with id {} has not been found", id);
            throw new Exception("Bank account has not been found");
        }
    }

    @Override
    public Collection<BankAccount> getAll() {
        log.info("Get all accounts");
        return this.cache;
    }
}
