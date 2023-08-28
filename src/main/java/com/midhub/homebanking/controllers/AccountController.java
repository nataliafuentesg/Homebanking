package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3 ) {
            return new ResponseEntity<>("You cannot have more than three accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = generateAccountNumber();

        while (accountRepository.findByNumber(accountNumber) !=  null) {
            accountNumber = generateAccountNumber();
        }

        Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        newAccount.setClient(client);

        accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

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
