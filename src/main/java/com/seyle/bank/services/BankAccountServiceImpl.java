package com.seyle.bank.services;

import com.google.common.collect.Lists;
import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.ActionType;
import com.seyle.bank.models.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        if (amount > 0) {
            this.addHistory(ActionType.DEPOSIT, newAccount.getId(), amount, null);
        }
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
            this.addHistory(ActionType.WITHDRAWAL, id, amount, null);
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
            this.addHistory(ActionType.DEPOSIT, id, amount, null);
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
                this.addHistory(ActionType.TRANSFER_TO, idPayer, amount, idPayee);

                payeeAccount.get().deposit(amount);
                this.addHistory(ActionType.TRANSFER_FROM, idPayee, amount, idPayer);
            } else {
                throw new BankAccountException("Payer account has not enough money, please make a deposit");
            }
        } else {
            throw new BankAccountException("Accounts were not found.");
        }
    }

    @Override
    public Collection<AccountHistory> getHistory(String id) {
        Optional<BankAccount> bankAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        if (bankAccount.isPresent()) {
            return bankAccount.get().getHistory();
        }
        return Lists.newArrayList();
    }

    private void addHistory(ActionType actionType, String id, Double amount, @Nullable String relatedAccount) {
        Optional<BankAccount> bankAccount = this.cache.stream().filter(b -> b.getId().equalsIgnoreCase(id)).findFirst();
        bankAccount.ifPresent(account -> account.addHistory(AccountHistory.builder()
                .actionType(actionType)
                .doneAt(LocalDateTime.now())
                .amount(amount)
                .relatedAccount(relatedAccount)
                .build()));
    }
}
