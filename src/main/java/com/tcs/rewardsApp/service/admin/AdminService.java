package com.tcs.rewardsApp.service.admin;

import com.tcs.rewardsApp.dto.request.CesUserRequest;
import com.tcs.rewardsApp.dto.response.UserResponse;

import java.util.List;

public interface AdminService {

    void createCesUser(CesUserRequest request);

    void deleteCesUser(Long userId);

    List<UserResponse> getAllUsers();
}
