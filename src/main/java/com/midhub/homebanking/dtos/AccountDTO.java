package com.midhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.Set;

@JsonPropertyOrder
public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    private Set<Transaction> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions();
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

    public Set<Transaction> getTransactions() {
        return transactions;
    }
}
