package com.tcs.rewardsApp.service.admin;

import com.tcs.rewardsApp.dto.request.CesUserRequest;

public interface AdminService {

    void createCesUser(CesUserRequest request);

    void deleteCesUser(Long userId);

    java.util.List<com.tcs.rewardsApp.dto.response.UserResponse> getAllUsers();
}
