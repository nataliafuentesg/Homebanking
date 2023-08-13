package com.midhub.homebanking;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			// save a couple of customers

			Client melbaMorel = clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			Client quioneGalvis = clientRepository.save(new Client("Quione", "Galvis", "quione@mindhub.com"));
			Client nataliaGalvis = clientRepository.save(new Client("Natalia", "Galvis", "nn@mindhub.com"));

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.0);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);
			Account account3 = new Account("VIN003", LocalDate.now().minusDays(2), 3000.0);
			Account account4 = new Account("VIN004", LocalDate.now().minusDays(3), 6000.0);
			Account account5 = new Account("VIN005", LocalDate.now().plusDays(5), 7500.0);
			Account account6 = new Account("VIN006", LocalDate.now().minusDays(8), 3000.0);
			Account account7 = new Account("VIN007", LocalDate.now().minusDays(12), 6000.0);

			melbaMorel.addAccount(account1);
			melbaMorel.addAccount(account2);
			quioneGalvis.addAccount(account3);
			quioneGalvis.addAccount(account4);
			nataliaGalvis.addAccount(account5);
			nataliaGalvis.addAccount(account6);
			nataliaGalvis.addAccount(account7);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);
			accountRepository.save(account6);
			accountRepository.save(account7);



			Transaction transaction1 = new Transaction(100.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account1);
			Transaction transaction2 = new Transaction(-50.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account2);
			Transaction transaction3 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account3);
			Transaction transaction4 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account4);
			Transaction transaction5 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT,"Credit Test", account5);
			Transaction transaction6 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account6);
			Transaction transaction7 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account7);
			Transaction transaction8 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account1);
			Transaction transaction9 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account1);
			Transaction transaction10 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account2);


			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);

			List<Loan> loans = Arrays.asList(
					new Loan("Hipotecario", 500000, Arrays.asList(12, 24, 36, 48, 60)),
					new Loan("Personal", 100000, Arrays.asList(6, 12, 24)),
					new Loan("Automotriz", 300000, Arrays.asList(6, 12, 24, 36))
			);

			loanRepository.saveAll(loans);

			Loan Hipotecario = loanRepository.findByName("Hipotecario");
			Loan Personal = loanRepository.findByName("Personal");
			Loan Automotriz = loanRepository.findByName("Automotriz");

			List<ClientLoan> clientLoans = Arrays.asList(
					new ClientLoan(400000, 60, melbaMorel, Hipotecario),
					new ClientLoan(50000, 12, melbaMorel, Personal),
					new ClientLoan(100000, 24, quioneGalvis, Personal),
					new ClientLoan(200000, 36, quioneGalvis, Automotriz)
			);


			clientLoanRepository.saveAll(clientLoans);

		};
	}
}
