package com.midhub.homebanking.services.implement;

import com.midhub.homebanking.dtos.LoanDTO;
import com.midhub.homebanking.models.Loan;
import com.midhub.homebanking.repositories.LoanRepository;
import com.midhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan findByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll()
                .stream()
                .map(LoanDTO::new)
                .collect(toList());
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
