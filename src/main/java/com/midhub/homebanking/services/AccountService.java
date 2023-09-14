package com.midhub.homebanking.services;

import com.midhub.homebanking.dtos.AccountDTO;
import com.midhub.homebanking.models.Account;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    Account findByNumber(String accountNumber);

    void saveAccount(Account account);

    List<AccountDTO> getAccounts();

    AccountDTO getAccount(Long id);


    Account findById(long id);
}
