package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.entity.enums.CustomerType;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private CustomerType customerType;
    private CustomerStatus status;
    private LocalDate doj;

}
