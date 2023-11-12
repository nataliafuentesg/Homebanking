package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.ClientDTO;

import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.AccountType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.AccountRepository;

import com.midhub.homebanking.repositories.ClientRepository;
import com.midhub.homebanking.services.AccountService;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;


@RequestMapping("/api")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication) {
        return clientService.getAuthenticatedClient(authentication);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }


        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        String accountNumber = generateAccountNumber();
        while (accountService.findByNumber(accountNumber) !=  null) {
            accountNumber = generateAccountNumber();
        }

        Account account = new Account(accountNumber, LocalDate.now(), 0.0, true, AccountType.SAVINGS);
        account.setClient(newClient);
        accountService.saveAccount(account);

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


