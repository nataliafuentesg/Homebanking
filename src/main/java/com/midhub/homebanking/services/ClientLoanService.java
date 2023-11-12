package com.midhub.homebanking.services;

import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.ClientLoan;

import java.util.Optional;

public interface ClientLoanService {
    boolean existsByClientAndLoanId(Client client, Long id);
    Optional<ClientLoan> findById(Long id);

    void saveClientLoan(ClientLoan clientLoan);
}
