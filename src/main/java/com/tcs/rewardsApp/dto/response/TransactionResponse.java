package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.enums.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
public class TransactionResponse {

    private String transactionId;
    private Integer amount;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private Integer rewardPoints;

}
