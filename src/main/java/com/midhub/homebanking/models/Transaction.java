package com.midhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double amount;
    private LocalDateTime date;
    private TransactionType transactionType;
    private String description;
    private double current_balance;
    private boolean activated = true;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction( double amount, LocalDateTime date, TransactionType transactionType, String descripiton, Account account, Double current_balance) {

        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
        this.description = descripiton;
        this.account = account;
        this.current_balance = account.getBalance() + amount;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType gettransactionType() {
        return transactionType;
    }

    public void settransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(double current_balance) {
        this.current_balance = current_balance;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
