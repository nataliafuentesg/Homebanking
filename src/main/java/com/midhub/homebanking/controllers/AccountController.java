package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.AccountType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.repositories.TransactionRepository;
import com.midhub.homebanking.services.AccountService;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import  com.midhub.homebanking.Utils.Utilities;



@RequestMapping("/api")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }


    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> newAccount(@RequestParam AccountType accountType, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You cannot have more than three accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = Utilities.generateAccountNumber();

        while (accountService.findByNumber(accountNumber) != null) {
            accountNumber = Utilities.generateAccountNumber();
        }

        Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setAccountType(accountType);
        newAccount.setBalance(0.0);
        newAccount.setClient(client);

        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping("/clients/current/accounts/deactivate")
    public ResponseEntity<String> deleteAccount(Authentication authentication, @RequestParam long id) {

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


}
