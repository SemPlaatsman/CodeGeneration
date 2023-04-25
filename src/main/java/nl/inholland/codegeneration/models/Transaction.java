package nl.inholland.codegeneration.models;

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
    @Column(name = "accountFrom", nullable = false, columnDefinition = "varchar(255)")
    private String accountFrom;
    @Column(name = "accountTo", nullable = false, columnDefinition = "varchar(255)")
    private String accountTo;
    @Column(name = "amount", nullable = false, columnDefinition = "Decimal(32,2)")
    private BigDecimal amount;
    @Column(name = "performingUser", nullable = false, columnDefinition = "long")
    private Long performingUser;
    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)")
    private String description;
    @Column(name = "isDeleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;
}    