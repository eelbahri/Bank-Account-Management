package com.seyle.bank.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class AccountHistory {
    @Builder.Default
    private String id = UUID.randomUUID().toString().substring(0, 8);

    private ActionType actionType;

    private Double amount;

    @Nullable
    private String relatedAccount;

    private LocalDateTime doneAt;
}
