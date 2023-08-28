package com.midhub.homebanking.repositories;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByClientAndCardTypeAndCardcolor(Client client, CardType cardType, CardColor cardcolor);
    long countByClientAndCardType(Client client, CardType cardType);



   Card findByNumber(String cardNumber);
}
