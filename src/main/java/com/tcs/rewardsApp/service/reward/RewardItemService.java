package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.RewardItemResponse;
import java.util.List;

public interface RewardItemService {
    List<RewardItemResponse> getAllRewardItems();
}
