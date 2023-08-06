package com.midhub.homebanking.repositories;
import java.util.List;

import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

}
