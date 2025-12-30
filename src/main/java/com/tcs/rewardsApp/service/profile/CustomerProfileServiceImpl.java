package com.tcs.rewardsApp.service.profile;

import com.tcs.rewardsApp.dto.response.*;
import com.tcs.rewardsApp.service.card.CreditCardService;
import com.tcs.rewardsApp.service.customer.CustomerService;
import com.tcs.rewardsApp.service.reward.RewardBalanceService;
import com.tcs.rewardsApp.service.reward.RedemptionHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CustomerProfileServiceImpl
        implements CustomerProfileService {

    private final CustomerService customerService;
    private final CreditCardService creditCardService;
    private final RewardBalanceService rewardBalanceService;
    private final RedemptionHistoryService redemptionHistoryService;

    public CustomerProfileServiceImpl(
            CustomerService customerService,
            CreditCardService creditCardService,
            RewardBalanceService rewardBalanceService,
            RedemptionHistoryService redemptionHistoryService
    ) {
        this.customerService = customerService;
        this.creditCardService = creditCardService;
        this.rewardBalanceService = rewardBalanceService;
        this.redemptionHistoryService = redemptionHistoryService;
    }

    @Override
    public CustomerProfileResponse getProfile(Long customerId) {

        CustomerProfileResponse profile = new CustomerProfileResponse();

        // 1️⃣ Customer basic details (always allowed, even for inactive)
        CustomerResponse customer =
                customerService.getCustomerById(customerId);
        profile.setCustomer(customer);

        // 2️⃣ Cards (only for active customers, graceful degradation for inactive)
        try {
            profile.setLinkedCards(
                    creditCardService.getCardsByCustomer(
                            customerId, false
                    )
            );

            profile.setUnlinkedCards(
                    creditCardService.getCardsByCustomer(
                                    customerId, true
                            ).stream()
                            .filter(c -> c.getStatus().name().equals("UNLINKED"))
                            .toList()
            );
        } catch (Exception e) {
            // Customer is inactive, return empty lists
            profile.setLinkedCards(new java.util.ArrayList<>());
            profile.setUnlinkedCards(new java.util.ArrayList<>());
        }

        // 3️⃣ Reward balance (only for active customers, graceful degradation for inactive)
        try {
            profile.setRewardBalance(
                    rewardBalanceService.getAvailablePoints(customerId)
            );
        } catch (Exception e) {
            // Customer is inactive, set balance to 0
            profile.setRewardBalance(0);
        }

        // 4️⃣ Redemption history (only for active customers, graceful degradation for inactive)
        try {
            Page<RedemptionHistoryResponse> history =
                    redemptionHistoryService.getHistory(
                            customerId, 0, 10
                    );
            profile.setRedemptionHistory(history.getContent());
        } catch (Exception e) {
            // Customer is inactive, return empty history
            profile.setRedemptionHistory(new java.util.ArrayList<>());
        }

        return profile;
    }
}
