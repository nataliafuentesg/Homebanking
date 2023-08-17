package com.midhub.homebanking.repositories;

import com.midhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
