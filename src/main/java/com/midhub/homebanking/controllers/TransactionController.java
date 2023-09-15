package com.midhub.homebanking.controllers;

import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.dtos.PaymentDTO;
import com.midhub.homebanking.models.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/transaction/payment")
    public ResponseEntity<Object> Payments(@RequestBody PaymentDTO payment){
        if (payment.getNumber().length() != 19) {
            return new ResponseEntity<>("Invalid Card Number", HttpStatus.FORBIDDEN);
        }
        if (payment.getAmount() <= 0) {
            return new ResponseEntity<>("Invalid Amount", HttpStatus.FORBIDDEN);
        }
        if (String.valueOf(payment.getCvv()).length() != 3) {
            return new ResponseEntity<>("Invalid Cvv", HttpStatus.FORBIDDEN);
        }
        if (payment.getDescription().isBlank()) {
            return new ResponseEntity<>("Empty Description", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.findByNumber(payment.getNumber());
        if (card.getCardType() != CardType.DEBIT) {
            return new ResponseEntity<>("Not a debit card", HttpStatus.FORBIDDEN);
        }
        if (card.getFromDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Card is expired!", HttpStatus.FORBIDDEN);
        }
        if (!String.valueOf(card.getCvv()).equals(String.valueOf(payment.getCvv()))) {
            return new ResponseEntity<>("Invalid Cvv", HttpStatus.FORBIDDEN);
        }
        Account account = card.getClient()
                .getAccounts()
                .stream()
                .filter(account1 ->
                        account1.getBalance() > payment.getAmount()).collect(Collectors.toList()).get(0);
        if (account.getBalance() < payment.getAmount()) {
            return new ResponseEntity<>("No account with sufficient balance.", HttpStatus.FORBIDDEN);
        }
        Transaction newTransaction = new Transaction(-payment.getAmount(), LocalDateTime.now(),
                TransactionType.DEBIT, payment.getDescription(), account, account.getBalance() - payment.getAmount());
        account.setBalance(account.getBalance() - payment.getAmount());
        transactionService.saveTransaction(newTransaction);
        accountService.saveAccount(account);
        return new ResponseEntity<>("Payment successful", HttpStatus.OK);

    }
}
