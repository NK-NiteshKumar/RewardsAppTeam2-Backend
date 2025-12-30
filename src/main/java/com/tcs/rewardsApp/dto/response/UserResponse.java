package com.tcs.rewardsApp.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private boolean active;
}
