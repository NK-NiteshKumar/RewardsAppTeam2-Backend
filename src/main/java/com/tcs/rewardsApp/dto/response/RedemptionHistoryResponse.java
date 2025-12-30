package com.tcs.rewardsApp.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter

public class RedemptionHistoryResponse {

    private Long redemptionId;
    private Integer pointsUsed;
    private LocalDateTime redeemedDate;
    private List<RedemptionItemResponse> items;

}
