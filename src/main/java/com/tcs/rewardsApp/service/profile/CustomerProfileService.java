package com.tcs.rewardsApp.service.profile;

import com.tcs.rewardsApp.dto.response.CustomerProfileResponse;

public interface CustomerProfileService {

    CustomerProfileResponse getProfile(Long customerId);
}
