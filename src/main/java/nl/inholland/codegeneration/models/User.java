package nl.inholland.codegeneration.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "role", nullable = false, columnDefinition = "smallint default 1")
    private Role role;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "varchar(255)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "firstName", nullable = false, columnDefinition = "varchar(255)")
    private String firstName;

    @Column(name = "lastName", nullable = false, columnDefinition = "varchar(255)")
    private String lastName;

    @Column(name = "email", nullable = false, columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "phoneNumber", nullable = false, columnDefinition = "varchar(255)")
    private String phoneNumber;

    @Column(name = "birthdate", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthdate;

    @Column(name = "dayLimit", nullable = false, columnDefinition = "Decimal(32,2) default '1000.00'")
    private BigDecimal dayLimit;

    @Column(name = "TransactionLimit", nullable = false, columnDefinition = "Decimal(32,2) default '200.00'")
    private BigDecimal transactionLimit;

    @Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    public User update(User user) {
        this.setRole(user.getRole());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setBirthdate(user.getBirthdate());
        this.setDayLimit(user.getDayLimit());
        this.setTransactionLimit(user.getTransactionLimit());
        return this;
    }
}
