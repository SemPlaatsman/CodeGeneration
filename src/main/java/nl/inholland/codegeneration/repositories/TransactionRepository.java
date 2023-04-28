package nl.inholland.codegeneration.repositories;

import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    
}
