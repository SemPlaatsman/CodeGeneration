package nl.inholland.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Column(name = "username", nullable = false, unique = true, precision = 255)
    private String username;

    @Column(name = "password", nullable = false, precision = 255)
    private String password;

    @Column(name = "firstName", nullable = false, precision = 255)
    private String firstName;

    @Column(name = "lastName", nullable = false, precision = 255)
    private String lastName;

    @Column(name = "email", nullable = false, precision = 255)
    private String email;

    @Column(name = "phoneNumber", nullable = false, precision = 255)
    private String phoneNumber;

    @Column(name = "birthdate", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthdate;

    @Column(name = "dayLimit", nullable = false, precision = 32, scale = 2)
    private BigDecimal dayLimit = new BigDecimal("1000");

    @Column(name = "transactionLimit", nullable = false, precision = 32, scale = 2)
    private BigDecimal transactionLimit = new BigDecimal("200");

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

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
