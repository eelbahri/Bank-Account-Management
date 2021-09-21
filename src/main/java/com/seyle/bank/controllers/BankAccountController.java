package com.seyle.bank.controllers;

import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.BankAccount;
import com.seyle.bank.services.BankAccountException;
import com.seyle.bank.services.BankAccountService;
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
    public Object deposit(@PathVariable String id, @PathVariable Double amount) throws BankAccountException {
        try {
            return this.bankAccountService.deposit(id, amount);
        } catch (BankAccountException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(value = "/withdraw/{id}/{amount}")
    @ResponseBody
    public Object withdraw(@PathVariable String id, @PathVariable Double amount) throws BankAccountException {
        try {
            return this.bankAccountService.withdraw(id, amount);
        } catch (BankAccountException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(value = "/transfer/{idPayer}/{idPayee}/{amount}")
    @ResponseBody
    public String transfer(@PathVariable String idPayer, @PathVariable String idPayee, @PathVariable Double amount) throws BankAccountException {
        try {
            this.bankAccountService.transfer(idPayer, idPayee, amount);
            return "Transfer successful";
        } catch (BankAccountException e) {
            return e.getMessage();
        }
    }

    @GetMapping(value = "/getHistory/{id}")
    @ResponseBody
    public Collection<AccountHistory> getHistory(@PathVariable String id) {
        return this.bankAccountService.getHistory(id);
    }
}
