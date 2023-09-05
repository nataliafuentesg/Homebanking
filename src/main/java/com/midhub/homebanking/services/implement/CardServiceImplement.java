package com.midhub.homebanking.services.implement;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.CardRepository;
import com.midhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;


    @Override
    public Card findByClientAndCardTypeAndCardcolor(Client client, CardType type, CardColor color) {
        return cardRepository.findByClientAndCardTypeAndCardcolor(client, type, color);
    }

    @Override
    public Card findByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
