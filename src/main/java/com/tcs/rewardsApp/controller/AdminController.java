package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.request.CesUserRequest;
import com.tcs.rewardsApp.service.admin.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/ces-users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCesUser(@Valid @RequestBody CesUserRequest request) {
        adminService.createCesUser(request);
    }

    @DeleteMapping("/ces-users/{id}")
    public void deleteCesUser(@PathVariable Long id) {
        adminService.deleteCesUser(id);
    }

}
