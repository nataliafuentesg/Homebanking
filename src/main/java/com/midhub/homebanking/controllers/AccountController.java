package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import com.midhub.homebanking.repositories.TransactionRepository;
import com.midhub.homebanking.services.AccountService;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3 ) {
            return new ResponseEntity<>("You cannot have more than three accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = generateAccountNumber();

        while (accountService.findByNumber(accountNumber) !=  null) {
            accountNumber = generateAccountNumber();
        }

        Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        newAccount.setClient(client);

        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping("/{accountNumber}/deactivate")
    @PutMapping ("/api/accounts/{id}")
    public ResponseEntity<String> deleteAccount(Authentication authentication,@PathVariable long id) {

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (client == null) {
            return new ResponseEntity<>("You can't delete an account because you're not a client.", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (account.getBalance() != 0.0) {
            return new ResponseEntity<>("The account can't be deleted because it has a balance different from 0.", HttpStatus.FORBIDDEN);
        }

        account.setActivated(false);
        accountService.saveAccount(account);

        List<Transaction> transactions = transactionRepository.findByAccountId(id);
        transactions.forEach(transaction -> {
            transaction.setActivated(false);
            transactionRepository.save(transaction);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


private String generateAccountNumber() {
        int accountNumber = getRandomNumberUsingNextInt(10000000, 99999999);
        return "VIN-" + accountNumber;
    }
    private int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
