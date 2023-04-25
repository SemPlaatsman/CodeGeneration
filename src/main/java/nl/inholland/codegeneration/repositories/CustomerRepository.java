package nl.inholland.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.inholland.codegeneration.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
