package com.midhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity

public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;

    private String email;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    @JsonManagedReference
    Set<Account> accounts = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<ClientLoan> clientLoans = new ArrayList<>();

    public void addLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    @JsonIgnore
    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    @JsonIgnore
    public List<Loan> getLoans() {
        return clientLoans.stream().map(sub -> sub.getLoan()).collect(toList());
    }

    public void setClientLoans(List<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public Client() { }

    public Client(String first, String last, String mail) {

        this.firstName = first;
        this.lastName = last;
        this.email = mail;
    }

    public Client(String firstName, String lastName, String email, Set<Account> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return firstName + " " + lastName + " " + email;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Client(String firstName, String lastName, String email, Set<Account> accounts, List<ClientLoan> clientLoans) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
        this.clientLoans = clientLoans;
    }
}
