package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.ClientDTO;
import com.midhub.homebanking.dtos.LoanApplicationDTO;
import com.midhub.homebanking.dtos.LoanDTO;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
import com.midhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanApplicationController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @RequestMapping(path = "/clients/current/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (!isValidLoanApplication(loanApplication)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid loan application data.");
        }

        Optional<Loan> loanOptional = loanService.findById(loanApplication.getLoanId());

        if (loanOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.");
        }

        boolean clientHasLoanOfSameType = clientLoanService.existsByClientAndLoanId(client, loanApplication.getLoanId());

        if (clientHasLoanOfSameType) {
            return new ResponseEntity<>("Client already has a loan of the same type", HttpStatus.BAD_REQUEST);
        }

        Loan loan = loanOptional.get();

        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested loan amount exceeds the maximum allowed.");
        }


        if (!loan.getPayments().contains(loanApplication.getInstallments())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number of installments for the selected loan.");
        }


        Account destinationAccount = accountService.findByNumber(loanApplication.getDestinationAccountNumber());

        if (destinationAccount == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destination account not found.");
        }


        if (destinationAccount.getClient().getId() != client.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not belong to the authenticated client.");
        }


        double loanAmount = loanApplication.getAmount() * 1.2;


        ClientLoan clientLoan = new ClientLoan(loanAmount, loanApplication.getInstallments(), client , loan);
        clientLoanService.saveClientLoan(clientLoan);


        Transaction creditTransaction = new Transaction(loanApplication.getAmount(), LocalDateTime.now(), TransactionType.CREDIT, loan.getName() + " loan approved", destinationAccount);
        transactionService.saveTransaction(creditTransaction);


        destinationAccount.setBalance(destinationAccount.getBalance() + loanAmount);
        accountService.saveAccount(destinationAccount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private boolean isValidLoanApplication(LoanApplicationDTO loanApplication) {
        return loanApplication != null &&
                loanApplication.getAmount() > 0 &&
                loanApplication.getInstallments() > 0 &&
                !loanApplication.getDestinationAccountNumber().isBlank();
    }

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

}
