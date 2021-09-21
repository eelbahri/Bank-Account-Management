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

    private final Collection<BankAccount> cache = Lists.newArrayList();

    @Override
    public BankAccount create(String owner, Double amount) {
        BankAccount newAccount = BankAccount.builder()
            .owner(owner)
            .amount(amount)
            .build();
        log.info("New Account added in cache : {}", newAccount.toString());
        this.cache.add(newAccount);
        return newAccount;
    }

    @Override
    public Optional<BankAccount> checkAccountAmount(String id) {
        log.info("Looking for account {}", id);
        return this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public BankAccount withdraw(String id, Double amount) throws BankAccountException {
        Optional<BankAccount> optAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        if (optAccount.isPresent()) {
            log.info("Withdraw of {} for Bank account {}", amount, id);
            BankAccount bankAccount = optAccount.get();
            log.info("bank amount : {} - withdraw {}. is possible ? {}", bankAccount.getAmount(), amount, bankAccount.getAmount() > amount);
            if (bankAccount.getAmount() < amount) {
                throw new BankAccountException("There is not enough on this account, please make a deposit");
            }
            bankAccount.withdraw(amount);
            log.info("Account {} has now {}", id, bankAccount.getAmount());
            return bankAccount;
        } else {
            log.error("Bank account with id {} has not been found", id);
            throw new BankAccountException("Bank account has not been found");
        }
    }

    @Override
    public BankAccount deposit(String id, Double amount) throws BankAccountException {
        Optional<BankAccount> optAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        if (optAccount.isPresent()) {
            log.info("Deposit of {} for account {}", amount, id);
            BankAccount bankAccount = optAccount.get();
            bankAccount.deposit(amount);
            log.info("Account {} has now {}", id, bankAccount.getAmount());
            return bankAccount;
        } else {
            log.error("Bank account with id {} has not been found", id);
            throw new BankAccountException("Bank account has not been found");
        }
    }

    @Override
    public Collection<BankAccount> getAll() {
        log.info("Get all accounts");
        return this.cache;
    }

    @Override
    public void transfer(String idPayer, String idPayee, Double amount) throws BankAccountException {
        Optional<BankAccount> payerAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(idPayer)).findFirst();
        Optional<BankAccount> payeeAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(idPayee)).findFirst();

        if (payerAccount.isPresent() && payeeAccount.isPresent()) {
            if (payerAccount.get().getAmount() > amount) {
                payerAccount.get().withdraw(amount);
                payeeAccount.get().deposit(amount);
            } else {
                throw new BankAccountException("Payer account has not enough money, please make a deposit");
            }
        } else {
            throw new BankAccountException("Accounts were not found.");
        }
    }
}
