package com.midhub.homebanking.models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.midhub.homebanking.repositories.AccountRepository;
import com.midhub.homebanking.repositories.ClientRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@SpringBootApplication
public class Client {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private long id;
        private String firstName;
        private String lastName;

        private String email;
        @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
        @JsonManagedReference
        Set<Account> accounts = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Account> getAccounts() {
        return accounts;
        }


        public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
        }

        public Client() { }

        public Client(String first, String last, String mail) {

            this.firstName = first;
            this.lastName = last;
            this.email = mail;
        }

    public Client(String firstName, String lastName, String email, Set<Account> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
    }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
        return email;
        }

        public void setEmail(String email) {
        this.email = email;
        }

        public String toString() {
            return firstName + " " + lastName + " " + email;
        }

         public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
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
