package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.CustomerRepository;
import com.tcs.rewardsApp.repository.RedemptionRepository;
import com.tcs.rewardsApp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class RewardBalanceServiceImpl implements RewardBalanceService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RedemptionRepository redemptionRepository;

    public RewardBalanceServiceImpl(
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository,
            RedemptionRepository redemptionRepository
    ) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @Override
    public int getAvailablePoints(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        int earned =
                transactionRepository.sumRewardPointsByCustomer(customerId);

        int redeemed =
                redemptionRepository.sumRedeemedPoints(customerId);

        int balance = earned - redeemed;

        return Math.max(balance, 0); // safety net
    }
}
