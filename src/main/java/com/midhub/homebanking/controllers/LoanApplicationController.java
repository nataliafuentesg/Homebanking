package com.midhub.homebanking.controllers;
import com.midhub.homebanking.dtos.*;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
import com.midhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/clients/current/loans")
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


        double loanAmount = loanApplication.getAmount() + ((loanApplication.getAmount() * loanApplication.getInterestRate())/100);


        ClientLoan clientLoan = new ClientLoan(loanApplication.getAmount(), loanApplication.getInstallments(),loanApplication.getInstallments(), loanAmount, client , loan);
        clientLoanService.saveClientLoan(clientLoan);


        Transaction creditTransaction = new Transaction(loanApplication.getAmount(), LocalDateTime.now(), TransactionType.CREDIT, loan.getName() + " loan approved", destinationAccount, destinationAccount.getBalance() + loanAmount);
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

    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody Loan loan, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Loan existingLoan = loanService.findByName(loan.getName());

        if (existingLoan != null) {
            return ResponseEntity.badRequest().body("Loan type with that name already exists.");
        }

        if (loan.getMaxAmount() < 1000 ) {
            return ResponseEntity.badRequest().body("Loan amount is not within the allowed range.");
        }

        if (loan.getPayments() == null || loan.getPayments().size() < 2) {
            return ResponseEntity.badRequest().body("The 'installments' array must have at least two elements.");
        }

        loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan Created Successfully",HttpStatus.CREATED);

    }
    @Transactional
    @PostMapping("/clients/current/loans/pay-installment")
    public ResponseEntity<Object> payInstallment(@RequestParam Long idLoan, @RequestParam String account, Authentication authentication) {


        if (authentication == null) {
            return new ResponseEntity<>("Client must be authenticated", HttpStatus.FORBIDDEN);
        }
        if (idLoan == null) {
            return new ResponseEntity<>("Missing loan information", HttpStatus.FORBIDDEN);
        }
        if (account.isBlank()) {
            return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Account accToPay = accountService.findByNumber(account);
        Optional<ClientLoan> clientLoanOptional = clientLoanService.findById(idLoan);
        ClientLoan clientLoan = clientLoanOptional.get();

        Double payment = clientLoan.getAmount() / clientLoan.getPayments();
        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (accToPay == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (accToPay.getBalance() < payment) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getRemainAmount() <= 0) {
            return new ResponseEntity<>("Fully paid loan", HttpStatus.FORBIDDEN);
        }
        Transaction newTransaction = new Transaction(payment, LocalDateTime.now(), TransactionType.DEBIT, "Loan Payment" ,accToPay,accToPay.getBalance() - payment);
        accToPay.setBalance(accToPay.getBalance() - payment);
        clientLoan.setRemainPayments(clientLoan.getRemainPayments() - 1);
        clientLoan.setRemainAmount(clientLoan.getRemainAmount() - payment);

        accountService.saveAccount(accToPay);
        clientLoanService.saveClientLoan(clientLoan);
        transactionService.saveTransaction(newTransaction);
        return new ResponseEntity<>("Installment paid successfully", HttpStatus.OK);
    }


}
