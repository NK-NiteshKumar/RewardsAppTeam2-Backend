package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.service.reward.RewardBalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/rewards")
public class RewardBalanceController {

    private final RewardBalanceService rewardBalanceService;

    public RewardBalanceController(RewardBalanceService rewardBalanceService) {
        this.rewardBalanceService = rewardBalanceService;
    }

    @GetMapping("/balance")
    public Map<String, Integer> getRewardBalance(
            @PathVariable Long customerId
    ) {
        int points = rewardBalanceService.getAvailablePoints(customerId);
        return Map.of("availablePoints", points);
    }
}
