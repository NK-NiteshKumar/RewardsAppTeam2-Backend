package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.response.RewardCartResponse;
import com.tcs.rewardsApp.service.reward.RewardCartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/cart")
public class RewardCartController {

    private final RewardCartService cartService;

    public RewardCartController(RewardCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items/{rewardItemId}")
    public void addItem(
            @PathVariable Long customerId,
            @PathVariable Long rewardItemId
    ) {
        cartService.addItem(customerId, rewardItemId);
    }

    @DeleteMapping("/items/{rewardItemId}")
    public void removeItem(
            @PathVariable Long customerId,
            @PathVariable Long rewardItemId
    ) {
        cartService.removeItem(customerId, rewardItemId);
    }

    @GetMapping
    public RewardCartResponse viewCart(
            @PathVariable Long customerId
    ) {
        return cartService.viewCart(customerId);
    }
}
