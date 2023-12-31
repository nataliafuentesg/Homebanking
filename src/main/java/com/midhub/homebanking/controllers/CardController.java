package com.midhub.homebanking.controllers;
import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.services.CardService;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import  com.midhub.homebanking.Utils.Utilities;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> newCard(@RequestParam CardType type, @RequestParam CardColor color,
                                          Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());

        Card existingCardWithTypeAndColor = cardService.findByClientAndCardTypeAndCardcolor(client, type, color);
        if (existingCardWithTypeAndColor != null) {
            return new ResponseEntity<>("A card with this type and color already exists", HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        int cvv = Utilities.generateRandomCVV();
        LocalDate startDate = LocalDate.now();
        LocalDate expirationDate = startDate.plusYears(5);
        do {
            cardNumber = Utilities.generateCardNumber();
        } while (cardService.findByNumber(cardNumber) != null);

        Card card = new Card(cardHolder, type, color, cardNumber, cvv, startDate, expirationDate, client, true);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card Created Successfully",HttpStatus.CREATED);

    }

    @PatchMapping("/clients/current/cards/deactivate")
    public ResponseEntity<Object> deactivateCard(@RequestParam Long cardId, Authentication authentication) {
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

    @GetMapping("/clients/current/credit-cards")
    public ResponseEntity<List<Card>> getCreditCardsForCurrentUser(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        List<Card> allCardsForClient = cardService.findByClient(client);

        List<Card> creditCards = allCardsForClient.stream()
                .filter(card -> card.getCardType() == CardType.CREDIT)
                .collect(Collectors.toList());

        return new ResponseEntity<>(creditCards, HttpStatus.OK);
    }



}
