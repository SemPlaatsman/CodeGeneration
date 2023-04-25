package nl.inholland.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.inholland.codegeneration.models.User;

@Repository
public interface CustomerRepository extends JpaRepository<User, Long> {
    
}
