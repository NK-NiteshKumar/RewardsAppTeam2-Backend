package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RedemptionRepository extends JpaRepository<Redemption, Long> {

    @Query("""
        SELECT COALESCE(SUM(r.pointsUsed), 0)
        FROM Redemption r
        WHERE r.customer.id = :customerId
    """)
    Integer sumRedeemedPoints(Long customerId);

    Page<Redemption> findByCustomerId(
            Long customerId,
            Pageable pageable
    );

}
