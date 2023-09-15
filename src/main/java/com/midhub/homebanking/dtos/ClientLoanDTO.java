package com.midhub.homebanking.dtos;
import com.midhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String loanName;
    private double loanAmount;
    private int loanPayments;
    private double interestRate;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.interestRate = clientLoan.getLoan().getInterestRate();
        this.loanAmount = clientLoan.getAmount();
        this.loanPayments = clientLoan.getPayments();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() { return loanId; }

    public String getLoanName() {
        return loanName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public int getLoanPayments() {
        return loanPayments;
    }
}
