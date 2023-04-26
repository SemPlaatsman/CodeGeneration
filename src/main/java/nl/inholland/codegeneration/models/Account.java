package nl.inholland.codegeneration.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "iban", nullable = false, unique = true, columnDefinition = "varchar(255)")
    private String iban;

    @Column(name = "accountType", nullable = false, columnDefinition = "enum('CURRENT', 'SAVINGS')")
    private AccountType accountType;

    @ManyToOne
    private User customer;
    
    @Column(name = "balance", nullable = false, columnDefinition = "Decimal(32,2) default '0.00'")
    private BigDecimal balance;

    @Column(name = "absoluteLimit", nullable = false, columnDefinition = "Decimal(32,2) default '1000.00'")
    private BigDecimal absoluteLimit;

    //generate getters and setters for these fields

 
}
