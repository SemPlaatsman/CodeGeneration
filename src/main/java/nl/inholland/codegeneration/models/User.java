package nl.inholland.codegeneration.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
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
    @Column(name = "isEmployee", nullable = false, columnDefinition = "boolean default false")
    private Boolean isEmployee;
    @Column(name = "isCustomer", nullable = false, columnDefinition = "boolean default true")
    private Boolean isCustomer;
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
}
