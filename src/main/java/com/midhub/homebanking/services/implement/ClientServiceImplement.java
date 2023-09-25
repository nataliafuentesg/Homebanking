package com.midhub.homebanking.services.implement;
import com.midhub.homebanking.dtos.ClientDTO;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.ClientRepository;
import com.midhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    @Override
    public ClientDTO getClient(Long id) {
        return clientRepository
                .findById(id)
                .map(ClientDTO::new)
                .orElse(null);
    }

    @Override
    public ClientDTO getAuthenticatedClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }


}
