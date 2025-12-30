package com.tcs.rewardsApp.service.card;

import com.tcs.rewardsApp.dto.request.CreditCardLinkRequest;
import com.tcs.rewardsApp.dto.response.CreditCardResponse;

import java.util.*;

public interface CreditCardService {

    void linkCard(
            Long customerId,
            CreditCardLinkRequest request,
            String createdBy
    );

    List<CreditCardResponse> getCardsByCustomer(
            Long customerId,
            boolean includeUnlinked
    );

    void unlinkCard(
            Long customerId,
            Long cardId,
            String modifiedBy
    );

    void relinkCard(
            Long customerId,
            Long cardId,
            String modifiedBy
    );

}
