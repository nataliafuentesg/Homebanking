package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.PaymentDTO;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.services.AccountService;
import com.midhub.homebanking.services.CardService;
import com.midhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequestMapping("/transaction")
@RestController
public class PaymentController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;
    @Transactional
    @PostMapping("/payment")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
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
