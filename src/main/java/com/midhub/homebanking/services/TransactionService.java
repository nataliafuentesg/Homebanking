package com.midhub.homebanking.services;

import com.midhub.homebanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
