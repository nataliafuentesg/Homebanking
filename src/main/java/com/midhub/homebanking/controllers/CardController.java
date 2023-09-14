package com.midhub.homebanking.controllers;
import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.CardRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import com.midhub.homebanking.services.CardService;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(@RequestParam CardType type, @RequestParam CardColor color,
                                          Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());

        Card existingCardWithTypeAndColor = cardService.findByClientAndCardTypeAndCardcolor(client, type, color);
        if (existingCardWithTypeAndColor != null) {
            return new ResponseEntity<>("A card with this type and color already exists", HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        int cvv = generateRandomCVV();
        LocalDate startDate = LocalDate.now();
        LocalDate expirationDate = startDate.plusYears(5);
        do {
            cardNumber = generateCardNumber();
        } while (cardService.findByNumber(cardNumber) != null);

        Card card = new Card(cardHolder, type, color, cardNumber, cvv, startDate, expirationDate, client, true);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card Created Successfully",HttpStatus.CREATED);

    }

    @PostMapping("/clients/current/cards/{cardId}/deactivate")
    public ResponseEntity<Object> deactivateCard(@PathVariable Long cardId, Authentication authentication) {
        Optional<Card> cardOptional = cardService.findById(cardId);

        if (cardOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found.");
        }

        Card card = cardOptional.get();
        Client client = clientService.findByEmail(authentication.getName());

        if (!card.getClient().equals(client)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Card does not belong to the authenticated client.");
        }
        card.setActivated(false);
        cardService.saveCard(card);

        return ResponseEntity.status(HttpStatus.OK).body("Card deactivated successfully.");
    }

    private String generateCardNumber() {
        return getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999);
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
