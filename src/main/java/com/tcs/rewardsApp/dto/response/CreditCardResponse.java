package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.enums.CardStatus;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
public class CreditCardResponse {

    private Long id;
    private String maskedCardNumber;
    private LocalDate expiryDate;
    private CardStatus status;

}
