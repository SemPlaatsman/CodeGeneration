package nl.inholland.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated
    @Column(name = "role", nullable = false, columnDefinition = "smallint default 1")
    private Role role;

    // @ElementCollection(fetch = FetchType.EAGER)
    // private List<Role> role;


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

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        // return true;
        return !this.getIsDeleted();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
