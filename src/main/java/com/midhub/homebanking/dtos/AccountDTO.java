package com.midhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


import com.midhub.homebanking.models.Account;
import java.time.LocalDate;


public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    private Set<TransactionDTO> transactions;
    private AccountType accountType;
    private boolean activated;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.activated = account.isActivated();
        this.accountType = account.getAccountType();
    }

    public long getId() {
        return id;
    }



    public String getNumber() {
        return number;
    }



    public LocalDate getCreationDate() {
        return creationDate;
    }



    public Double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean isActivated() {
        return activated;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
