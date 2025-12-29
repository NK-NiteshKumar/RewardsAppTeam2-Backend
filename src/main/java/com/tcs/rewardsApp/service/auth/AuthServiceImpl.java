package com.tcs.rewardsApp.service.auth;

import com.tcs.rewardsApp.entity.User;
import com.tcs.rewardsApp.repository.UserRepository;
import com.tcs.rewardsApp.security.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtTokenProvider tokenProvider,
            BCryptPasswordEncoder encoder
    ) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
    }

    @Override
    public Map<String, String> login(Map<String, String> payload) {

        // 🔥 TEMPORARY: NO AES (PLAIN TEXT)
        String username = payload.get("username");
        String password = payload.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token =
                tokenProvider.generateToken(user.getUsername(), user.getRole());

        return Map.of(
                "token", token,
                "role", user.getRole()
        );
    }
}
