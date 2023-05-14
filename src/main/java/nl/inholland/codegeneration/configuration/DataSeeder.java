package nl.inholland.codegeneration.configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import nl.inholland.codegeneration.models.Account;
import nl.inholland.codegeneration.models.AccountType;
import nl.inholland.codegeneration.models.Role;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import nl.inholland.codegeneration.repositories.AccountRepository;
import nl.inholland.codegeneration.repositories.TransactionRepository;
import nl.inholland.codegeneration.repositories.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Test data
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Users
        userRepository.save(new User(null, Role.Employee, "johndoe", "john123", "John", "Doe", "john.doe@gmail.com",
                "0671122334", LocalDate.of(2003, 9, 1), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "sarawilson", "sara123", "Sara", "Wilson", "sara.wilson@yahoo.com",
                        "0612345678", LocalDate.of(1990, 11, 13), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "tomlee", "tom123", "Tom", "Lee", "tom.lee@hotmail.com",
                "0642154678", LocalDate.of(1978, 7, 6), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "janesmith", "jane123", "Jane", "Smith", "jane.smith@gmail.com",
                        "0651239876", LocalDate.of(2000, 3, 8), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "bown", "bob123", "", "", "bob.brown@hotmail.com",
                "0620123456", LocalDate.of(1995, 9, 21), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "maryjo", "mary123", "Mary", "Johnson", "mary.johnson@yahoo.com",
                        "0612345678", LocalDate.of(1992, 12, 18), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "tomsmith", "tom123", "Tom", "Smith", "tom.smith@hotmail.com",
                "0636549871", LocalDate.of(1987, 8, 4), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "saradoe", "sara123", "Sara", "Doe", "sara.doe@gmail.com",
                "0687654321", LocalDate.of(1975, 6, 17), new BigDecimal(1000), new BigDecimal(200), false));
        userRepository.save(new User(null, Role.Customer, "johnwilson", "john123", "John", "Wilson", "john.wilson@yahoo.com",
                        "0654321098", LocalDate.of(1983, 4, 29), new BigDecimal(1000), new BigDecimal(200), false));
        // Transactions
        // transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4,
        // 26, 13, 8, 0), ));

        // Accounts
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(1L).orElseThrow(),
                new BigDecimal("120"), new BigDecimal("-1000")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(1L).orElseThrow(),
                new BigDecimal("436000"), new BigDecimal("-1000")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(2L).orElseThrow(),
                new BigDecimal("520"), new BigDecimal("-2400")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(3L).orElseThrow(),
                new BigDecimal("11"), new BigDecimal("-2600")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(4L).orElseThrow(),
                new BigDecimal("-25"), new BigDecimal("-300")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(4L).orElseThrow(),
                new BigDecimal("300"), new BigDecimal("-1200")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(4L).orElseThrow(),
                new BigDecimal("12000000"), new BigDecimal("-2600")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(5L).orElseThrow(),
                new BigDecimal("110"), new BigDecimal("-2500")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(6L).orElseThrow(),
                new BigDecimal("1000"), new BigDecimal("-1000")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(6L).orElseThrow(),
                new BigDecimal("1000"), new BigDecimal("-1000")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(7L).orElseThrow(),
                new BigDecimal("-1100"), new BigDecimal("-2200")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(7L).orElseThrow(),
                new BigDecimal("1200"), new BigDecimal("-500")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(8L).orElseThrow(),
                new BigDecimal("30000"), new BigDecimal("-6000")));
        accountRepository.save(new Account(null, AccountType.CURRENT, userRepository.findById(9L).orElseThrow(),
                new BigDecimal("340"), new BigDecimal("-1500")));
        accountRepository.save(new Account(null, AccountType.SAVINGS, userRepository.findById(9L).orElseThrow(),
                new BigDecimal("600"), new BigDecimal("-1000")));

        List<Account> accounts = accountRepository.findAll();
        // Transactions
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 27, 18, 53, 11), accounts.get(0), accounts.get(1), new BigDecimal("100"), userRepository.findById(1L).orElseThrow(), "Die verdraaide belastingdienst!"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 27, 13, 28, 1), accounts.get(2), accounts.get(3), new BigDecimal("5.50"), userRepository.findById(2L).orElseThrow(), "Sushibox"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 27, 13, 26, 54), accounts.get(4), accounts.get(12), new BigDecimal("20000"), userRepository.findById(8L).orElseThrow(), "Beretta AR70/90"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 27, 11, 45, 23), accounts.get(8), accounts.get(0), new BigDecimal("12.50"), userRepository.findById(6L).orElseThrow(), "Bowlen"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 26, 15, 47, 47), accounts.get(4), accounts.get(6), new BigDecimal("230000"), userRepository.findById(4L).orElseThrow(), "Pizzo Maranzano, Basilicata, Veneto"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 25, 5, 33, 53), accounts.get(10), accounts.get(4), new BigDecimal("100000"), userRepository.findById(7L).orElseThrow(), "Maranzano pizzo"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 25, 3, 26, 25), accounts.get(13), accounts.get(4), new BigDecimal("70000"), userRepository.findById(9L).orElseThrow(), "Basilicata pizzo"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 24, 23, 52, 2), accounts.get(0), accounts.get(4), new BigDecimal("50000"), userRepository.findById(1L).orElseThrow(), "Veneto pizzo"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 24, 23, 13, 11), accounts.get(11), accounts.get(10), new BigDecimal("37.20"), userRepository.findById(7L).orElseThrow(), "Broodje ham"));
        transactionRepository.save(new Transaction(null, LocalDateTime.of(2023, 4, 24, 12, 54, 10), accounts.get(13), accounts.get(12), new BigDecimal("12.50"), userRepository.findById(9L).orElseThrow(), "Kaas"));
    }
}
