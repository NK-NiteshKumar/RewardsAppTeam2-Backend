package com.tcs.rewardsApp.service.customer;

import com.tcs.rewardsApp.dto.request.CustomerCreateRequest;
import com.tcs.rewardsApp.dto.request.CustomerUpdateRequest;
import com.tcs.rewardsApp.dto.response.CustomerListResponse;
import com.tcs.rewardsApp.dto.response.CustomerResponse;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import org.springframework.data.domain.Page;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest request, String createdBy);

    Page<CustomerListResponse> getCustomers(
            int page,
            int size,
            String search,
            CustomerStatus status
    );

    CustomerResponse updateCustomer(
            Long customerId,
            CustomerUpdateRequest request,
            String modifiedBy
    );

    void softDeleteCustomer(Long customerId, String modifiedBy);

    void activateCustomer(Long customerId, String modifiedBy);

    CustomerResponse getCustomerById(Long customerId);
}
