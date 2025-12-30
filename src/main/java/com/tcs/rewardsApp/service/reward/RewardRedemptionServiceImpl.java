package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.entity.*;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardRedemptionServiceImpl
        implements RewardRedemptionService {

    private final CustomerRepository customerRepository;
    private final RewardCartRepository cartRepository;
    private final RewardCartItemRepository cartItemRepository;
    private final RewardBalanceService rewardBalanceService;
    private final RedemptionRepository redemptionRepository;
    private final RedemptionItemRepository redemptionItemRepository;

    public RewardRedemptionServiceImpl(
            CustomerRepository customerRepository,
            RewardCartRepository cartRepository,
            RewardCartItemRepository cartItemRepository,
            RewardBalanceService rewardBalanceService,
            RedemptionRepository redemptionRepository,
            RedemptionItemRepository redemptionItemRepository
    ) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.rewardBalanceService = rewardBalanceService;
        this.redemptionRepository = redemptionRepository;
        this.redemptionItemRepository = redemptionItemRepository;
    }

    @Override
    @Transactional
    public void redeemCart(Long customerId, String redeemedBy) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        RewardCart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException("CART_EMPTY"));

        List<RewardCartItem> cartItems =
                cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new BusinessException("CART_EMPTY");
        }

        int cartPoints =
                cartItems.stream()
                        .mapToInt(
                                i -> i.getQuantity()
                                        * i.getRewardItem().getPointsCost()
                        )
                        .sum();

        int availablePoints =
                rewardBalanceService.getAvailablePoints(customerId);

        if (availablePoints < cartPoints) {
            throw new BusinessException("INSUFFICIENT_REWARD_POINTS");
        }

        Redemption redemption = new Redemption();
        redemption.setCustomer(customer);
        redemption.setPointsUsed(cartPoints);
        redemption.setRedeemedDate(LocalDateTime.now());
        redemption.setRedeemedBy(redeemedBy);

        redemption = redemptionRepository.save(redemption);

        for (RewardCartItem cartItem : cartItems) {

            RedemptionItem ri = new RedemptionItem();
            ri.setRedemption(redemption);
            ri.setRewardItem(cartItem.getRewardItem());
            ri.setQuantity(cartItem.getQuantity());
            ri.setPointsCost(cartItem.getRewardItem().getPointsCost());

            redemptionItemRepository.save(ri);
        }

        cartItemRepository.deleteByCart(cart);
    }
}
