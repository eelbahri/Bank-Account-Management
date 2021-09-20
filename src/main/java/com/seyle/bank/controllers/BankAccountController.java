package com.seyle.bank.controllers;

import com.seyle.bank.models.BankAccount;
import com.seyle.bank.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/v1/bankAccount")
@CrossOrigin
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Collection<BankAccount>> getAll() {
        Collection<BankAccount> all = this.bankAccountService.getAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping(value = "/create/{owner}/{amount}")
    public ResponseEntity<Object> create(@PathVariable String owner, @PathVariable Double amount) {
        this.bankAccountService.create(owner, amount);
        return ResponseEntity.ok().build();
    }
}
