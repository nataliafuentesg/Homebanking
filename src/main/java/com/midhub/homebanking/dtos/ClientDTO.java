package com.midhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;

import java.util.Set;

@JsonPropertyOrder
public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Account> accounts;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts();
    }

    public long getId() {
        return id;
    }



    public String getFirstName() {
        return firstName;
    }



    public String getLastName() {
        return lastName;
    }



    public String getEmail() {
        return email;
    }



    public Set<Account> getAccounts() {
        return accounts;
    }


}
