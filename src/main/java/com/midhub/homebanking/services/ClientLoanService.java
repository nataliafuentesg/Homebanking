package com.midhub.homebanking.services;

import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.ClientLoan;

public interface ClientLoanService {
    boolean existsByClientAndLoanId(Client client, Long id);

    void saveClientLoan(ClientLoan clientLoan);
}
