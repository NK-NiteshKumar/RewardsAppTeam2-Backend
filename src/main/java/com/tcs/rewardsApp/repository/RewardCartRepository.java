package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.RewardCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardCartRepository extends JpaRepository<RewardCart, Long> {

    Optional<RewardCart> findByCustomer(Customer customer);
}
