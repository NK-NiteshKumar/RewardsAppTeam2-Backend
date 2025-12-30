package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhoneNo(String phoneNo);

    Page<Customer> findByStatus(
            CustomerStatus status,
            Pageable pageable
    );

    Page<Customer> findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
            CustomerStatus status1,
            String firstName,
            CustomerStatus status2,
            String lastName,
            Pageable pageable
    );
}
