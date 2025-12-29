package com.tcs.rewardsApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward_carts")
@Getter
@Setter
public class RewardCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
