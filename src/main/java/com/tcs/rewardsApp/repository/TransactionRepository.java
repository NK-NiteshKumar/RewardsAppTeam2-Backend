package com.tcs.rewardsApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tcs.rewardsApp.entity.CreditCard;
import com.tcs.rewardsApp.entity.Transaction;
import com.tcs.rewardsApp.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByTransactionId(String transactionId);

    List<Transaction> findByCreditCardAndStatus(
            CreditCard creditCard,
            TransactionStatus status
    );

    List<Transaction> findByCreditCard(CreditCard creditCard);

    Page<Transaction> findByCreditCard(
            CreditCard creditCard,
            Pageable pageable
    );

    @Query("""
    SELECT COALESCE(SUM(t.rewardPoints), 0)
    FROM Transaction t
    WHERE t.creditCard.customer.id = :customerId
      AND t.status = 'PROCESSED'
    """)
    Integer sumRewardPointsByCustomer(Long customerId);

}
