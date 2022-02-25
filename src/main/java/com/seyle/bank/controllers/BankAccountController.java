package com.seyle.bank.controllers;

import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.BankAccount;
import com.seyle.bank.services.BankAccountException;
import com.seyle.bank.services.BankAccountService;
import com.seyle.bank.services.InsufficientFundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public BankAccount create(@PathVariable String owner, @PathVariable Double amount) {
        return this.bankAccountService.create(owner, amount);
    }

    @PostMapping(value = "/deposit/{id}/{amount}")
    @ResponseBody
    public void deposit(@PathVariable String id, @PathVariable Double amount) throws BankAccountException {
        this.bankAccountService.deposit(id, amount);
    }

    @PostMapping(value = "/withdraw/{id}/{amount}")
    @ResponseBody
    public void withdraw(@PathVariable String id, @PathVariable Double amount) throws BankAccountException, InsufficientFundException {
        this.bankAccountService.withdraw(id, amount);
    }

    @GetMapping(value = "/getHistory/{id}")
    @ResponseBody
    public Collection<AccountHistory> getHistory(@PathVariable String id) throws BankAccountException {
        return this.bankAccountService.getHistory(id);
    }

    @PostMapping(value = "/transfer/{fromAccountId}/{toAccountId}/{amount}")
    public void transfer(@PathVariable String fromAccountId, @PathVariable String toAccountId, @PathVariable Double amount) throws Exception {
        this.bankAccountService.transfer(fromAccountId, toAccountId, amount);
    }
}
