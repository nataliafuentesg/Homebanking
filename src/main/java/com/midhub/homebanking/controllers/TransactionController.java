package com.midhub.homebanking.controllers;

import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.models.TransactionType;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.TransactionRepository;
import com.midhub.homebanking.services.AccountService;
import com.midhub.homebanking.services.CardService;
import com.midhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/api")
@RestController
public class TransactionController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @Transactional
    @RequestMapping(path = "/clients/current/transactions/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> Transaction(@RequestParam String fromAccountNumber,
                                              @RequestParam String toAccountNumber,
                                              @RequestParam Double amount,
                                              @RequestParam String description,
                                              Authentication authentication){

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Source and destination accounts cannot be the same", HttpStatus.BAD_REQUEST);
        }

        Account fromAccount = accountService.findByNumber(fromAccountNumber);
        Account toAccount = accountService.findByNumber(toAccountNumber);

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

        Transaction debitTransaction = new Transaction(-amount, transactionDate, TransactionType.DEBIT, description , fromAccount, fromAccount.getBalance() - amount);
        Transaction creditTransaction = new Transaction(amount, transactionDate, TransactionType.CREDIT, description , toAccount, toAccount.getBalance() + amount);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        accountService.saveAccount(fromAccount);
        accountService.saveAccount(toAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/transaction/payment")
    public ResponseEntity<Object> Payments(@RequestBody Transaction transaction){

    }
}
