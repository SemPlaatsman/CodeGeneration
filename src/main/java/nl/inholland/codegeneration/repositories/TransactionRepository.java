package nl.inholland.codegeneration.repositories;

import nl.inholland.codegeneration.models.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.accountFrom.iban = :iban OR t.accountTo.iban = :iban")
    List<Transaction> findAllByAccountFromIban(@Param("iban") String iban);

}
