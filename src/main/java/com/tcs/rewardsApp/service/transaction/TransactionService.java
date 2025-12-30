package com.tcs.rewardsApp.service.transaction;

import com.tcs.rewardsApp.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {

    void generateTransactions(Long customerId, Long cardId, String createdBy);

    Page<TransactionResponse> getTransactionsByCard(
            Long customerId,
            Long cardId,
            int page,
            int size
    );

    void processRewards(
            Long customerId,
            Long cardId,
            String processedBy
    );

}
