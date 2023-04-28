package nl.inholland.codegeneration.repositories;

import nl.inholland.codegeneration.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import nl.inholland.codegeneration.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {
    
}
