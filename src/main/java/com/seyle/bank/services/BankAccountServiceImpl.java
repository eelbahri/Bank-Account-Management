package com.seyle.bank.services;

import com.google.common.collect.Maps;
import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.ActionType;
import com.seyle.bank.models.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private static final Object tieLock = new Object();
    private final Map<String, BankAccount> cache = Maps.newHashMap();

    @Override
    public BankAccount create(String owner, Double amount) {
        BankAccount newAccount = BankAccount.builder()
                .owner(owner)
                .balance(amount)
                .build();
        log.info("New Account added in cache : {}", newAccount.toString());
        this.cache.put(newAccount.getId(), newAccount);
        return newAccount;
    }

    @Override
    public Optional<BankAccount> checkAccountAmount(String id) {
        log.info("Looking for account {}", id);
        if (this.cache.containsKey(id)) {
            return Optional.of(this.cache.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void withdraw(String id, Double amount) throws BankAccountException, InsufficientFundException {
        if (!this.cache.containsKey(id)) {
            throw new BankAccountException("Bank account has not been found");
        }

        BankAccount account = this.cache.get(id);
        log.info("Withdraw {} for account {}", amount, id);
        if (account.getBalance().compareTo(amount) > 0) {
            throw new InsufficientFundException("Account " + id + " does not have enough money");
        }
        account.withdraw(amount);
        log.info("Account {} has now {}", id, account.getBalance());
    }

    @Override
    public void deposit(String id, Double amount) throws BankAccountException {
        if (!this.cache.containsKey(id)) {
            throw new BankAccountException("Bank account has not been found");
        }

        BankAccount account = this.cache.get(id);
        log.info("Deposit of {} for account {}", amount, id);
        account.deposit(amount);
        log.info("Account {} has now {}", id, account.getBalance());
    }

    @Override
    public Collection<BankAccount> getAll() {
        return this.cache.values();
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, Double amount) throws BankAccountException, InsufficientFundException {

        if (!this.cache.containsKey(fromAccountId)) {
            throw new BankAccountException("Account {}" + fromAccountId + " was not found");
        }

        if (!this.cache.containsKey(toAccountId)) {
            throw new BankAccountException("Account {}" + toAccountId + " was not found");
        }

        BankAccount fromAccount = this.cache.get(fromAccountId);
        BankAccount toAccount = this.cache.get(toAccountId);
        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);

        if (fromHash < toHash) {
            synchronized (fromAccount) {
                this.execTransfer(fromAccount, toAccount, amount);
            }
        } else if (fromHash > toHash) {
            synchronized (toAccount) {
                this.execTransfer(fromAccount, toAccount, amount);
            }
        } else {
            synchronized (this.tieLock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        this.execTransfer(fromAccount, toAccount, amount);
                    }
                }
            }
        }
    }

    private void execTransfer(BankAccount fromAccount, BankAccount toAccount, Double amount) throws InsufficientFundException {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundException("Account " + fromAccount + " does not have enough money");
        } else {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
        }
    }

    @Override
    public Collection<AccountHistory> getHistory(String id) throws BankAccountException {
        if (this.cache.containsKey(id)) {
            return this.cache.get(id).getHistory();
        }
        throw new BankAccountException("Account " + id + " not found");
    }

    private void addHistory(ActionType actionType, String id, Double amount, @Nullable String relatedAccount) throws BankAccountException {
        if (this.cache.containsKey(id)) {
            BankAccount account = this.cache.get(id);
            account.addHistory(AccountHistory.builder()
                    .actionType(actionType)
                    .doneAt(LocalDateTime.now())
                    .amount(amount)
                    .relatedAccount(relatedAccount)
                    .build());
        }
        throw new BankAccountException("Account " + id + " not found");
    }
}
