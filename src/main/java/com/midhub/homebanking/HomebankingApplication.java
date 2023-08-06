package com.midhub.homebanking;

import com.midhub.homebanking.models.Account;
import com.midhub.homebanking.models.Client;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
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
		};
	}
}
