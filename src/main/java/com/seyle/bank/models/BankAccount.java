package com.seyle.bank.models;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class BankAccount {
    private String id = UUID.randomUUID().toString().substring(0, 8);
    private String owner;
    private Double amount;
    private Collection<AccountHistory> history;
}
