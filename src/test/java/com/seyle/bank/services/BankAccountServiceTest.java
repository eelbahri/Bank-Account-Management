package com.seyle.bank.services;

import com.seyle.bank.models.AccountHistory;
import com.seyle.bank.models.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class BankAccountServiceTest {

    @Autowired
    @InjectMocks
    private BankAccountService bankAccountService = new BankAccountServiceImpl();

    @Test
    public void testSuccessfulCreation() {
        this.bankAccountService.create("Foo", 1500d);
        Assertions.assertEquals(1, this.bankAccountService.getAll().size());
    }

    @Test
    public void testTwoAccountsAndCheckCache() {
        this.bankAccountService.create("Foo", 1500d);
        this.bankAccountService.create("Bar", 1500d);
        Assertions.assertEquals(2, this.bankAccountService.getAll().size());
    }

    @Test
    public void testSuccessfulDeposit() throws BankAccountException {
        BankAccount ba = this.bankAccountService.create("Foo", 1500d);
        Double firstAmount = ba.getBalance();
        Double deposit = 1500d;
        this.bankAccountService.deposit(ba.getId(), deposit);
        Assertions.assertEquals(ba.getBalance(), firstAmount + deposit);
    }

    @Test
    public void testSuccessfulWithdraw() throws BankAccountException, InsufficientFundException {
        Double initialAmount = 1500d;
        Double withdraw = 1000d;

        BankAccount ba = this.bankAccountService.create("Foo", initialAmount);
        this.bankAccountService.withdraw(ba.getId(), withdraw);
        Assertions.assertEquals(ba.getBalance(), initialAmount - withdraw);
    }

    @Test
    public void testWithdrawWithAmountTooHigh_ShouldThrowBankAccountException() throws BankAccountException {
        Double initialAmount = 1500d;
        Double withdraw = 2000d;

        BankAccount ba = this.bankAccountService.create("Foo", initialAmount);
        Assertions.assertThrows(BankAccountException.class, () -> {
            this.bankAccountService.withdraw(ba.getId(), withdraw);
        });
    }

    @Test
    public void testSuccessfulTransfer() throws Exception {
        Double initialAmount = 1000d;
        BankAccount ba1 = this.bankAccountService.create("Foo", initialAmount);
        BankAccount ba2 = this.bankAccountService.create("Bar", initialAmount);

        Double transferAmount = 500d;
        this.bankAccountService.transfer(ba1.getId(), ba2.getId(), transferAmount);

        Assertions.assertEquals(ba1.getBalance(), initialAmount - transferAmount);
        Assertions.assertEquals(ba2.getBalance(), initialAmount + transferAmount);
    }

    @Test
    public void testTransferWithAmountTooHigh_ShouldThrowBankAccountException() throws BankAccountException {
        Double initialAmount = 1000d;
        BankAccount ba1 = this.bankAccountService.create("Foo", initialAmount);
        BankAccount ba2 = this.bankAccountService.create("Bar", initialAmount);

        Double transferAmount = 2000d;

        Assertions.assertThrows(BankAccountException.class, () -> {
            this.bankAccountService.transfer(ba1.getId(), ba2.getId(), transferAmount);
        });
    }

    @Test
    public void testAddHistory() throws BankAccountException, InsufficientFundException {
        BankAccount ba = this.bankAccountService.create("Foo", 500d);
        this.bankAccountService.deposit(ba.getId(), 500d);
        this.bankAccountService.withdraw(ba.getId(), 1000d);
        Collection<AccountHistory> history = this.bankAccountService.getHistory(ba.getId());

        Assertions.assertEquals(3, history.size());
    }
}
