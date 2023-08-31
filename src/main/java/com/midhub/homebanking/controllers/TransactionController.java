package com.midhub.homebanking.controllers;

import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.models.TransactionType;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/api")
@RestController
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/clients/current/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> Transaction(@RequestParam String fromAccountNumber,
                                              @RequestParam String toAccountNumber,
                                              @RequestParam Double amount,
                                              @RequestParam String description,
                                              Authentication authentication){

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Source and destination accounts cannot be the same", HttpStatus.BAD_REQUEST);
        }

        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            return new ResponseEntity<>("One or both accounts do not exist", HttpStatus.BAD_REQUEST);
        }
        Client client = fromAccount.getClient();
        if (!client.getEmail().equals(authentication.getName())) {
            return new ResponseEntity<>("Authenticated user does not own the source account", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds in the source account", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime transactionDate = LocalDateTime.now();

        Transaction debitTransaction = new Transaction(-amount, transactionDate, TransactionType.DEBIT, description , fromAccount);
        Transaction creditTransaction = new Transaction(amount, transactionDate, TransactionType.CREDIT, description , toAccount);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
