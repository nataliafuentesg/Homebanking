package com.midhub.homebanking;
import com.midhub.homebanking.models.*;
import com.midhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {


	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers


			Client melbaMorel = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("abc123"));
			Client quioneGalvis = new Client("Quione", "Galvis", "quione@mindhub.com", passwordEncoder.encode("def456"));


			Client admin = new Client("Admin", "User", "admin@mindhubbrothers.com", "admin");
			admin.setPassword(passwordEncoder.encode(admin.getPassword()));

			clientRepository.save(melbaMorel);
			clientRepository.save(quioneGalvis);
			clientRepository.save(admin);

			Account account1 = new Account("VIN-00000001", LocalDate.now(), 5000.0, true, AccountType.SAVINGS);
			Account account2 = new Account("VIN-00000002", LocalDate.now().plusDays(1), 7500.0, true, AccountType.SAVINGS);
			Account account3 = new Account("VIN-00000003", LocalDate.now().minusDays(2), 3000.0, true, AccountType.SAVINGS);
			Account account4 = new Account("VIN-00000004", LocalDate.now().minusDays(3), 6000.0, true, AccountType.SAVINGS);

			melbaMorel.addAccount(account1);
			melbaMorel.addAccount(account2);
			quioneGalvis.addAccount(account3);
			quioneGalvis.addAccount(account4);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);



			Transaction transaction1 = new Transaction(100.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account1, 10.0);
			Transaction transaction2 = new Transaction(-50.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account2, 10.0);
			Transaction transaction3 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account3, 10.0);
			Transaction transaction4 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account4, 10.0);
			Transaction transaction8 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account1, 10.0);
			Transaction transaction9 = new Transaction(200.0, LocalDateTime.now(), TransactionType.CREDIT, "Credit Test", account1, 10.0);
			Transaction transaction10 = new Transaction(-75.0, LocalDateTime.now(), TransactionType.DEBIT, "Debit Test", account2, 10.0);


			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);

			List<Loan> loans = Arrays.asList(
					new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60),15),
					new Loan("Personal", 100000, Arrays.asList(6, 12, 24),15),
					new Loan("Car", 300000, Arrays.asList(6, 12, 24, 36),15)
			);

			loanRepository.saveAll(loans);

			Loan Mortgage = loanRepository.findByName("Mortgage");
			Loan Personal = loanRepository.findByName("Personal");
			Loan Car = loanRepository.findByName("Car");

			List<ClientLoan> clientLoans = Arrays.asList(
					new ClientLoan(400000, 60, 60, 400000, melbaMorel, Mortgage),
					new ClientLoan(50000, 12, 60, 50000, melbaMorel, Personal),
					new ClientLoan(100000, 24, 24, 100000, quioneGalvis, Personal),
					new ClientLoan(200000, 36, 36, 200000, quioneGalvis, Car)
			);


			clientLoanRepository.saveAll(clientLoans);

			Card debitCardForMelba = new Card(melbaMorel.getFirstName() +" "+ melbaMorel.getLastName(),
					CardType.DEBIT, CardColor.GOLD, "8990-1234-8907-4557", 567, LocalDate.now(),
					LocalDate.now().plusYears(5), melbaMorel, true);
			cardRepository.save(debitCardForMelba);


			Card creditCardForMelba = new Card(melbaMorel.getFirstName() +" "+ melbaMorel.getLastName(),
					CardType.CREDIT, CardColor.TITANIUM, "8990-1234-8907-8976", 997, LocalDate.now(),
					LocalDate.now().plusYears(5), melbaMorel, true);
			cardRepository.save(creditCardForMelba);

			Card creditCardForQuione = new Card(quioneGalvis.getFirstName() +" "+ quioneGalvis.getLastName(),
					CardType.CREDIT, CardColor.TITANIUM, "8990-1234-8907-1735", 997, LocalDate.now(),
					LocalDate.now().plusYears(5), quioneGalvis, true);
			cardRepository.save(creditCardForQuione);



		};
	}
}
