package com.midhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.midhub.homebanking.models.Account;

import java.time.LocalDate;
@JsonPropertyOrder
public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
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


}
