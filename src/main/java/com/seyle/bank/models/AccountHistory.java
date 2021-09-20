package com.seyle.bank.models;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountHistory {
    private String id = UUID.randomUUID().toString().substring(0, 8);
    private ActionType actionType;
    private Double amount;
}
