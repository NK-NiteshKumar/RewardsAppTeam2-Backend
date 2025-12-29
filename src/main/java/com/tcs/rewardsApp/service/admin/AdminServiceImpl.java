package com.tcs.rewardsApp.service.admin;

import com.tcs.rewardsApp.dto.request.CesUserRequest;
import com.tcs.rewardsApp.entity.User;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER =
            LogManager.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createCesUser(CesUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            LOGGER.warn("CES user already exists: {}", request.getUsername());
            throw new BusinessException("CES_USER_ALREADY_EXISTS");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole("CES");
        user.setActive(true);
        user.setCreatedBy("ADMIN");

        userRepository.save(user);

        LOGGER.info("CES user created successfully: {}", request.getUsername());
    }

    @Override
    public void deleteCesUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("CES_USER_NOT_FOUND"));

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            LOGGER.error("Attempt to delete ADMIN user");
            throw new BusinessException("CANNOT_DELETE_ADMIN");
        }

        // 🔒 Hook for future rule:
        // if (customerRepository.existsByCreatedBy(user.getUsername())) { ... }

        userRepository.delete(user);

        LOGGER.info("CES user deleted successfully: {}", user.getUsername());
    }
}
