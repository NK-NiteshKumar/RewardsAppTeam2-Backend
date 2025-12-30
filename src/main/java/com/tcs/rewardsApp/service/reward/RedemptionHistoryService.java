package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.RedemptionHistoryResponse;
import org.springframework.data.domain.Page;

public interface RedemptionHistoryService {

    Page<RedemptionHistoryResponse> getHistory(
            Long customerId,
            int page,
            int size
    );
}
