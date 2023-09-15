package com.midhub.homebanking.services;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CardService {
    Card findByClientAndCardTypeAndCardcolor(Client client, CardType type, CardColor color);

    Card findByNumber(String cardNumber);

    void saveCard(Card card);

    Optional<Card> findById(Long cardId);


}
