package com.tcs.rewardsApp.dto.response;

import lombok.*;

@Getter
@Setter
public class RewardCartItemResponse {

    private Long rewardItemId;
    private String name;
    private Integer quantity;
    private Integer pointsCost;
    private Integer totalPoints;
}
