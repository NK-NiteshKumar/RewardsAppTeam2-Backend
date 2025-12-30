package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.response.RewardItemResponse;
import com.tcs.rewardsApp.service.reward.RewardItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardItemController {

    private final RewardItemService rewardItemService;

    public RewardItemController(RewardItemService rewardItemService) {
        this.rewardItemService = rewardItemService;
    }

    @GetMapping
    public List<RewardItemResponse> getAllRewardItems() {
        return rewardItemService.getAllRewardItems();
    }
}
