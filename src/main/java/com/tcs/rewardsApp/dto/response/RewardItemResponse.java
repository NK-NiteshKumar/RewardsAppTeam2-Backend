package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.enums.RewardCategory;
import lombok.Data;

@Data
public class RewardItemResponse {
    private Long id;
    private String name;
    private RewardCategory category;
    private Integer pointsCost;
}
