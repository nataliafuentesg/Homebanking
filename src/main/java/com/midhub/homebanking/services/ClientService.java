package com.midhub.homebanking.services;

import com.midhub.homebanking.dtos.ClientDTO;
import com.midhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClientsDTO();

    ClientDTO getClient(Long id);
    ClientDTO getAuthenticatedClient(Authentication authentication);

    Client findByEmail(String email);

    void saveClient(Client client);
}
