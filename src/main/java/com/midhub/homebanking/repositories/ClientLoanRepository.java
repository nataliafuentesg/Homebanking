package com.midhub.homebanking.repositories;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    boolean existsByClientAndLoanId(Client client, long loanId);
    Optional<ClientLoan> findById(Long id);

}
