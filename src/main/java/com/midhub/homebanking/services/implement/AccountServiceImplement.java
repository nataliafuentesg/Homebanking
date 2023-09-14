package com.midhub.homebanking.services.implement;

import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account findByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id);
    }


}
