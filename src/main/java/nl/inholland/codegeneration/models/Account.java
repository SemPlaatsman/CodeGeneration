package nl.inholland.codegeneration.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GenericGenerator(name = "ibangen", strategy = "nl.inholland.codegeneration.services.IBANGenerator")
    @GeneratedValue(generator = "ibangen")
    @Column(name = "iban")
    private String iban;

    @Enumerated(EnumType.STRING)
    @Column(name = "accountType", nullable = false, columnDefinition = "enum('CURRENT', 'SAVINGS')")
    private AccountType accountType;

    @ManyToOne
    private User customer;
    
    @Column(name = "balance", nullable = false, columnDefinition = "Decimal(32,2) default '0.00'")
    private BigDecimal balance;

    @Column(name = "absoluteLimit", nullable = false, columnDefinition = "Decimal(32,2) default '1000.00'")
    private BigDecimal absoluteLimit;
}
