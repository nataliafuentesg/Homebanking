package com.midhub.homebanking.services.implement;

import com.midhub.homebanking.models.Transaction;
import com.midhub.homebanking.repositories.TransactionRepository;
import com.midhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
