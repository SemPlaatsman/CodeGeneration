package nl.inholland.codegeneration.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "role", nullable = false, columnDefinition = "smallint default 1")
    private Role role;

    // @ElementCollection(fetch = FetchType.EAGER)
    // private List<Role> role;


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public boolean isAccountNonExpired() {
        // return true;
        return this.getIsDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }



    @Override
    public boolean isEnabled() {
        return true;
    }

   

  
}
