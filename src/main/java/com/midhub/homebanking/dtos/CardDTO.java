package com.midhub.homebanking.dtos;

import com.midhub.homebanking.models.Card;
import com.midhub.homebanking.models.CardColor;
import com.midhub.homebanking.models.CardType;
import com.midhub.homebanking.models.Transaction;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private boolean activated;

    public CardDTO (Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardcolor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.activated = card.isActivated();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public boolean isActivated() {
        return activated;
    }
}
