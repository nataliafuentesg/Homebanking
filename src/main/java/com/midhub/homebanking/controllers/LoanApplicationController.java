package com.midhub.homebanking.controllers;

import com.midhub.homebanking.dtos.ClientDTO;
import com.midhub.homebanking.dtos.LoanApplicationDTO;
import com.midhub.homebanking.dtos.LoanDTO;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
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
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(path = "/clients/current/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplication, Authentication authentication) {

        // Get authenticated client
        Client client = clientRepository.findByEmail(authentication.getName());


        // Validate input data
        if (!isValidLoanApplication(loanApplication)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid loan application data.");
        }

        // Find the loan by ID
        Optional<Loan> loanOptional = loanRepository.findById(loanApplication.getLoanId());

        if (loanOptional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.");
        }

        boolean clientHasLoanOfSameType = clientLoanRepository.existsByClientAndLoanId(client, loanApplication.getLoanId());

        if (clientHasLoanOfSameType) {
            return new ResponseEntity<>("Client already has a loan of the same type", HttpStatus.BAD_REQUEST);
        }


        Loan loan = loanOptional.get();

        // Check if the requested amount exceeds the maximum allowed
        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requested loan amount exceeds the maximum allowed.");
        }

        // Verify the number of installments
        if (!loan.getPayments().contains(loanApplication.getInstallments())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number of installments for the selected loan.");
        }

        // Find the destination account
        Account destinationAccount = accountRepository.findByNumber(loanApplication.getDestinationAccountNumber());

        if (destinationAccount == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destination account not found.");
        }

        // Verify that the destination account belongs to the authenticated client
        if (destinationAccount.getClient().getId() != client.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not belong to the authenticated client.");
        }

        // Calculate the loan amount (original amount + 20%)
        double loanAmount = loanApplication.getAmount() * 1.2;

        // Create a loan application record
        ClientLoan clientLoan = new ClientLoan(loanAmount, loanApplication.getInstallments(), client , loan);
        clientLoanRepository.save(clientLoan);

        // Create a transaction of type "CREDIT" for the destination account
        Transaction creditTransaction = new Transaction(loanApplication.getAmount(), LocalDateTime.now(), TransactionType.CREDIT, loan.getName() + " loan approved", destinationAccount);
        transactionRepository.save(creditTransaction);

        // Update the destination account's balance
        destinationAccount.setBalance(destinationAccount.getBalance() + loanAmount);
        accountRepository.save(destinationAccount);
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
        return loanRepository.findAll()
                .stream()
                .map(LoanDTO::new)
                .collect(toList());
    }

}
