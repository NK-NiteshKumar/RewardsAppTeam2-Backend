package com.tcs.rewardsApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "redemption_items")
@Getter
@Setter
public class RedemptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "redemption_id")
    private Redemption redemption;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reward_item_id")
    private RewardItem rewardItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer pointsCost;
}
