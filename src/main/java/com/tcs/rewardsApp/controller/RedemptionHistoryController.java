package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.response.RedemptionHistoryResponse;
import com.tcs.rewardsApp.service.reward.RedemptionHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/redemptions")
public class RedemptionHistoryController {

    private final RedemptionHistoryService historyService;

    public RedemptionHistoryController(
            RedemptionHistoryService historyService
    ) {
        this.historyService = historyService;
    }

    @GetMapping
    public Page<RedemptionHistoryResponse> getHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return historyService.getHistory(customerId, page, size);
    }
}
