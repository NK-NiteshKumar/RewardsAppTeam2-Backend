package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.entity.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;
    private LocalDate doj;
    private CustomerType customerType;
    private CustomerStatus status;
}
