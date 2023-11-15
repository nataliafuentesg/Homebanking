package com.midhub.homebanking;

import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.List;

/*
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTests {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    private List<Client> clients = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    @Test
    public void existLoans() {
        loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }
    @Test
    public void existPersonalLoan() {
        loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }
    @Test
    public void existClients() {
        clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }
    @Test
    public void clientHasLastname() {
        clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("lastName", is(not(empty())))));
    }
    @Test
    public void existAccounts() {
        accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void accountExist() {
        accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", startsWith("VIN"))));
    }
    @Test
    public void existCards() {
        cards = cardRepository.findAll();
        assertThat(cards, hasItem(is(not(empty()))));
    }
    @Test
    public void cardHasCorrectNumber() {
        cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", hasLength(19))));
    }
    @Test
    public void existTransactions() {
        transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }
    @Test
    public void transactionPositive() {
        transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("amount", greaterThan(0.0))));
    }
    @Test
    public void transactionHasDate() {
        transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("date", is(not(empty())))));
    }
}*/
