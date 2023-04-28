package nl.inholland.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name="account_from", nullable = false, referencedColumnName = "iban")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @JoinColumn(name = "account_from", nullable = false)
    private Account accountFrom;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name="account_to", nullable = false, referencedColumnName = "iban")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne
    @JoinColumn(name = "account_to", nullable = false)
    private Account accountTo;

    @Column(name = "amount", nullable = false, columnDefinition = "Decimal(32,2)")
    private BigDecimal amount;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User performingUser;

    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)")
    private String description;

    public Transaction update(Transaction transaction) {
        this.setTimestamp(transaction.getTimestamp());
        this.setAccountFrom(transaction.getAccountFrom());
        this.setAccountTo(transaction.getAccountTo());
        this.setAmount(transaction.getAmount());
        this.setPerformingUser(transaction.getPerformingUser());
        this.setDescription(transaction.getDescription());
        return this;
    }
}    