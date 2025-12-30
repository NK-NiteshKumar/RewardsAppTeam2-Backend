package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.service.reward.RewardRedemptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/rewards")
public class RewardRedemptionController {

    private final RewardRedemptionService redemptionService;

    public RewardRedemptionController(
            RewardRedemptionService redemptionService
    ) {
        this.redemptionService = redemptionService;
    }

    @PostMapping("/redeem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void redeemCart(
            @PathVariable Long customerId,
            Principal principal
    ) {
        redemptionService.redeemCart(
                customerId,
                principal.getName()
        );
    }

}
