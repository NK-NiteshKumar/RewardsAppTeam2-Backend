package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.response.CustomerProfileResponse;
import com.tcs.rewardsApp.service.profile.CustomerProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/profile")
public class CustomerProfileController {

    private final CustomerProfileService profileService;

    public CustomerProfileController(CustomerProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public CustomerProfileResponse getProfile(@PathVariable Long customerId) {
        return profileService.getProfile(customerId);
    }
}