package com.tcs.rewardsApp.service.auth;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(Map<String, String> payload);
}
