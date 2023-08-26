package com.midhub.homebanking.repositories;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    long countByClientAndType(Client client, CardType type);

    Card findByClientAndColor(Client client, CardColor color);

    Card findByNumber(String cardNumber);
}
