package com.tcs.rewardsApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "redemptions")
@Getter
@Setter
public class Redemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private Integer pointsUsed;

    private LocalDateTime redeemedDate;

    private String redeemedBy;

    @OneToMany(mappedBy = "redemption", cascade = CascadeType.ALL)
    private List<RedemptionItem> items;
}
