package com.tcs.rewardsApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reward_cart_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cart_id", "reward_item_id"})
        })
@Getter
@Setter
public class RewardCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private RewardCart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reward_item_id")
    private RewardItem rewardItem;

    @Column(nullable = false)
    private Integer quantity;
}
