package com.seyle.bank.models;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class BankAccount {

    @Builder.Default
    private String id = UUID.randomUUID().toString().substring(0, 10);

    private String owner;

    private Double balance;
    
    @Builder.Default
    private Collection<AccountHistory> history = Lists.newArrayList();

    public void deposit(Double depositAmount) {
        this.balance += depositAmount;
    }

    public void withdraw(Double withdrawAmount) {
        this.balance -= withdrawAmount;
    }

    public void addHistory(AccountHistory history) {
        this.history.add(history);
    }
}
