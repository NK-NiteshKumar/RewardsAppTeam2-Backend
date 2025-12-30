package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.dto.request.CustomerCreateRequest;
import com.tcs.rewardsApp.dto.request.CustomerUpdateRequest;
import com.tcs.rewardsApp.dto.response.CustomerListResponse;
import com.tcs.rewardsApp.dto.response.CustomerResponse;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.service.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(
            @Valid @RequestBody CustomerCreateRequest request,
            Principal principal
    ) {
        return customerService.createCustomer(request, principal.getName());
    }

    @GetMapping
    public Page<CustomerListResponse> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ACTIVE") CustomerStatus status
    ) {
        if (size < 1) {
            throw new BusinessException("PAGE_SIZE_MUST_BE_GREATER_THAN_ZERO");
        }
        if (page < 0) {
            throw new BusinessException("PAGE_NUMBER_CANNOT_BE_NEGATIVE");
        }

        return customerService.getCustomers(page, size, search, status);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request,
            Principal principal
    ) {
        return customerService.updateCustomer(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(
            @PathVariable Long id,
            Principal principal
    ) {
        customerService.softDeleteCustomer(id, principal.getName());
    }

    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateCustomer(
            @PathVariable Long id,
            Principal principal
    ) {
        customerService.activateCustomer(id, principal.getName());
    }
}
