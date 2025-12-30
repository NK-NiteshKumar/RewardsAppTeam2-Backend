package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.CreditCard;
import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    boolean existsByCardNumber(String cardNumber);

    List<CreditCard> findByCustomerAndStatus(Customer customer, CardStatus status);

    List<CreditCard> findByCustomer(Customer customer);
}
