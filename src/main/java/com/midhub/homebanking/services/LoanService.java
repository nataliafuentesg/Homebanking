package com.midhub.homebanking.services;

import com.midhub.homebanking.dtos.LoanDTO;
import com.midhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Optional<Loan> findById(Long id);
    Loan findByName (String name);

    List<LoanDTO> getLoans();

    void saveLoan(Loan loan);
}
