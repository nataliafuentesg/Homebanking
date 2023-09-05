package com.midhub.homebanking.services.implement;

import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.ClientLoan;
import com.midhub.homebanking.repositories.ClientLoanRepository;
import com.midhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public boolean existsByClientAndLoanId(Client client, Long id) {
        return clientLoanRepository.existsByClientAndLoanId(client , id);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
