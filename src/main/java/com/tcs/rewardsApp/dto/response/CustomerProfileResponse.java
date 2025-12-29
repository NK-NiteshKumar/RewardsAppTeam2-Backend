package com.tcs.rewardsApp.dto.response;

import lombok.*;
import java.util.*;

@Getter
@Setter
public class CustomerProfileResponse {

    private CustomerResponse customer;

    private List<CreditCardResponse> linkedCards;
    private List<CreditCardResponse> unlinkedCards;

    private int rewardBalance;

    private List<RedemptionHistoryResponse> redemptionHistory;

}
