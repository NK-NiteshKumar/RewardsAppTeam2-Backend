package com.tcs.rewardsApp.dto.response;

import lombok.*;

@Getter
@Setter
public class RedemptionItemResponse {

    private String rewardItemName;
    private Integer quantity;
    private Integer pointsCost;

}
