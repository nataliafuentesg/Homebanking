package com.midhub.homebanking.controllers;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.CardRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(@RequestParam CardType type, @RequestParam CardColor color,
                                          Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        long existingCardsCount = cardRepository.countByClientAndType(client, type);
        if (existingCardsCount >= 3) {
            return new ResponseEntity<>("Maximum number of cards of this type reached", HttpStatus.FORBIDDEN);
        }

        Card existingCardWithColor = cardRepository.findByClientAndColor(client, color);
        if (existingCardWithColor != null) {
            return new ResponseEntity<>("A card with this color already exists", HttpStatus.FORBIDDEN);
        }

        String cardNumber = generateCardNumber();
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        int cvv = generateRandomCVV();
        LocalDate startDate = LocalDate.now();
        LocalDate expirationDate = startDate.plusYears(5);

        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.findByNumber(cardNumber) != null);

        Card card = new Card(cardHolder, type, color, cardNumber, cvv, startDate, expirationDate, client);
        cardRepository.save(card);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    private String generateCardNumber() {
        int accountNumber = getRandomNumberUsingNextInt(10000000, 99999999);
        return "VIN-" + accountNumber;
    }

    private int generateRandomCVV() {
        int randomCVV = getRandomNumberUsingNextInt(100, 999);
        return randomCVV;
    }
    private int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
