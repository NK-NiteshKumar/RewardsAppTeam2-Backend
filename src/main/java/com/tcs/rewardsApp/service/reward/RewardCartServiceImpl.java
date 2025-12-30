package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.RewardCartItemResponse;
import com.tcs.rewardsApp.dto.response.RewardCartResponse;
import com.tcs.rewardsApp.entity.*;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardCartServiceImpl implements RewardCartService {

    private final CustomerRepository customerRepository;
    private final RewardCartRepository cartRepository;
    private final RewardCartItemRepository cartItemRepository;
    private final RewardItemRepository rewardItemRepository;

    public RewardCartServiceImpl(
            CustomerRepository customerRepository,
            RewardCartRepository cartRepository,
            RewardCartItemRepository cartItemRepository,
            RewardItemRepository rewardItemRepository
    ) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.rewardItemRepository = rewardItemRepository;
    }

    @Override
    public void addItem(Long customerId, Long rewardItemId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new BusinessException("REWARD_ITEM_NOT_FOUND"));

        RewardCart cart = cartRepository
                .findByCustomer(customer)
                .orElseGet(() -> createCart(customer));

        RewardCartItem cartItem = cartItemRepository
                .findByCartAndRewardItem(cart, rewardItem)
                .orElseGet(() -> {
                    RewardCartItem item = new RewardCartItem();
                    item.setCart(cart);
                    item.setRewardItem(rewardItem);
                    item.setQuantity(0);
                    return item;
                });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepository.save(cartItem);

        cart.setModifiedDate(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public void removeItem(Long customerId, Long rewardItemId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        RewardCart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException("CART_NOT_FOUND"));

        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new BusinessException("REWARD_ITEM_NOT_FOUND"));

        RewardCartItem cartItem = cartItemRepository
                .findByCartAndRewardItem(cart, rewardItem)
                .orElseThrow(() -> new BusinessException("ITEM_NOT_IN_CART"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        cart.setModifiedDate(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public RewardCartResponse viewCart(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        RewardCart cart = cartRepository
                .findByCustomer(customer)
                .orElseThrow(() -> new BusinessException("CART_EMPTY"));

        List<RewardCartItem> items = cartItemRepository.findByCart(cart);

        return RewardCartResponse.from(cart, items);
    }

    private RewardCart createCart(Customer customer) {

        RewardCart cart = new RewardCart();
        cart.setCustomer(customer);
        cart.setCreatedDate(LocalDateTime.now());
        return cartRepository.save(cart);
    }
}
