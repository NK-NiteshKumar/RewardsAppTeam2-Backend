package com.tcs.rewardsApp.service.customer;

import com.tcs.rewardsApp.dto.request.CustomerCreateRequest;
import com.tcs.rewardsApp.dto.request.CustomerUpdateRequest;
import com.tcs.rewardsApp.dto.response.CustomerListResponse;
import com.tcs.rewardsApp.dto.response.CustomerResponse;
import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.entity.enums.CustomerType;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest request, String createdBy) {

        if (customerRepository.existsByPhoneNo(request.getPhoneNo())) {
            throw new BusinessException("CUSTOMER_PHONE_ALREADY_EXISTS");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDob(request.getDob());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setEmail(request.getEmail());
        customer.setEmail(request.getEmail());
        
        if (request.getDoj() != null) {
            customer.setDoj(request.getDoj());
        } else {
            customer.setDoj(LocalDate.now());
        }

        customer.setCustomerType(determineCustomerType(customer.getDoj()));
        customer.setStatus(CustomerStatus.ACTIVE);

        customer.setCreatedBy(createdBy);
        customer.setCreatedDate(LocalDateTime.now());

        return mapToResponse(customerRepository.save(customer));
    }

    private CustomerType determineCustomerType(LocalDate doj) {
        long years = ChronoUnit.YEARS.between(doj, LocalDate.now());
        return years >= 3 ? CustomerType.PREMIUM : CustomerType.REGULAR;
    }

    @Override
    public Page<CustomerListResponse> getCustomers(
            int page,
            int size,
            String search,
            CustomerStatus status
    ) {

        PageRequest pageable =
                PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Customer> customers;

        if (search != null && !search.isBlank()) {
            customers =
                    customerRepository
                            .findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
                                    status, search,
                                    status, search,
                                    pageable
                            );
        } else {
            customers =
                    customerRepository.findByStatus(status, pageable);
        }

        return customers.map(this::mapToListResponse);
    }

    private CustomerListResponse mapToListResponse(Customer customer) {

        CustomerListResponse response = new CustomerListResponse();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhoneNo(customer.getPhoneNo());
        response.setCustomerType(customer.getCustomerType());
        response.setStatus(customer.getStatus());
        response.setDoj(customer.getDoj());
        return response;
    }

    @Override
    public CustomerResponse updateCustomer(
            Long customerId,
            CustomerUpdateRequest request,
            String modifiedBy
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() == CustomerStatus.INACTIVE) {
            throw new BusinessException("CUSTOMER_ALREADY_INACTIVE");
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setModifiedBy(modifiedBy);
        customer.setModifiedDate(LocalDateTime.now());

        return mapToResponse(customerRepository.save(customer));
    }

    @Override
    public void softDeleteCustomer(Long customerId, String modifiedBy) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() == CustomerStatus.INACTIVE) {
            throw new BusinessException("CUSTOMER_ALREADY_INACTIVE");
        }

        customer.setStatus(CustomerStatus.INACTIVE);
        customer.setModifiedBy(modifiedBy);
        customer.setModifiedDate(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Override
    public void activateCustomer(Long customerId, String modifiedBy) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() == CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_ALREADY_ACTIVE");
        }

        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setModifiedBy(modifiedBy);
        customer.setModifiedDate(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        return mapToResponse(
                customerRepository.findById(customerId)
                        .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"))
        );
    }

    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhoneNo(customer.getPhoneNo());
        response.setEmail(customer.getEmail());
        response.setDob(customer.getDob());
        response.setDoj(customer.getDoj());
        response.setCustomerType(customer.getCustomerType());
        response.setStatus(customer.getStatus());
        return response;
    }
}
