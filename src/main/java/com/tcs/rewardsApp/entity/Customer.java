package com.tcs.rewardsApp.entity;

import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.entity.enums.CustomerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone_no")
        }
)
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------
    // PERSONAL DETAILS
    // -------------------------
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "phone_no", nullable = false, length = 10)
    private String phoneNo;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate doj; // Bank joining date

    // -------------------------
    // BUSINESS FIELDS
    // -------------------------
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    // -------------------------
    // AUDIT FIELDS
    // -------------------------
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
