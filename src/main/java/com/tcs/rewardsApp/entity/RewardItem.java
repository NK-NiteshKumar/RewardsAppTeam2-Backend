package com.tcs.rewardsApp.entity;

import com.tcs.rewardsApp.entity.enums.RewardCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reward_items")
@Getter
@Setter
public class RewardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardCategory category;

    @Column(nullable = false)
    private Integer pointsCost;

    @Column(nullable = false)
    private boolean active = true; // future-proof
}
