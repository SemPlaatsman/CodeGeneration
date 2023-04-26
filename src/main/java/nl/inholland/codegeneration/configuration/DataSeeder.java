package nl.inholland.codegeneration.configuration;

import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import nl.inholland.codegeneration.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    // Test data
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Users
        userRepository.save(new User(null, Role.Employee, "johndoe", "john123", "John", "Doe", "john.doe@gmail.com", "0671122334", LocalDate.of(2003, 9, 1), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "sarawilson", "sara123", "Sara", "Wilson", "sara.wilson@yahoo.com", "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "tomlee", "tom123", "Tom", "Lee", "tom.lee@hotmail.com", "0642154678", LocalDate.of(1978, 7, 6), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "janesmith", "jane123", "Jane", "Smith", "jane.smith@gmail.com", "0651239876", LocalDate.of(2000, 3, 8), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "bown", "bob123", "", "", "bob.brown@hotmail.com", "0620123456", LocalDate.of(1995, 9, 21), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "maryjo", "mary123", "Mary", "Johnson", "mary.johnson@yahoo.com", "0612345678", LocalDate.of(1992, 12, 18), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "tomsmith", "tom123", "Tom", "Smith", "tom.smith@hotmail.com", "0636549871", LocalDate.of(1987, 8, 4), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "saradoe", "sara123", "Sara", "Doe", "sara.doe@gmail.com", "0687654321", LocalDate.of(1975, 6, 17), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "johnwilson", "john123", "John", "Wilson", "john.wilson@yahoo.com", "0654321098", LocalDate.of(1983, 4, 29), new BigDecimal(1000), new BigDecimal(200), false));

        // Transactions
//        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 26, 13, 8, 0), new BigDecimal(200), "Je moeder"));
    }
}
