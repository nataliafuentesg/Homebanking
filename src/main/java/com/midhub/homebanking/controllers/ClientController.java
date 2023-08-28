package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.ClientDTO;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.ClientRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDTO::new)
                .collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository
                .findById(id)
                .map(ClientDTO::new)
                .orElse(null);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }


        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(newClient);

        String accountNumber = generateAccountNumber();
        while (accountRepository.findByNumber(accountNumber) !=  null) {
            accountNumber = generateAccountNumber();
        }

        Account account = new Account(accountNumber, LocalDate.now(), 0.0);
        account.setClient(newClient);
        accountRepository.save(account);

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


