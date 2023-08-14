package com.midhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;


public class TransactionDTO {

    private long id;
    private double amount;
    private LocalDateTime date;
    private TransactionType transactionType;
    public TransactionDTO (Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.transactionType = transaction.gettransactionType();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
