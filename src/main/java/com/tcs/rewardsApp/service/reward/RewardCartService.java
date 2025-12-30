package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.RewardCartResponse;

public interface RewardCartService {

    void addItem(Long customerId, Long rewardItemId);

    void removeItem(Long customerId, Long rewardItemId);

    RewardCartResponse viewCart(Long customerId);
}
