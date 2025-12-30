package com.tcs.rewardsApp.dto.response;

import com.tcs.rewardsApp.entity.RewardCart;
import com.tcs.rewardsApp.entity.RewardCartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RewardCartResponse {

    private Long customerId;
    private List<RewardCartItemResponse> items;
    private Integer totalPoints;

    public static RewardCartResponse from(
            RewardCart cart,
            List<RewardCartItem> cartItems
    ) {
        RewardCartResponse response = new RewardCartResponse();
        response.customerId = cart.getCustomer().getId();

        response.items = cartItems.stream().map(item -> {
            RewardCartItemResponse r = new RewardCartItemResponse();
            r.setRewardItemId(item.getRewardItem().getId());
            r.setName(item.getRewardItem().getName());
            r.setQuantity(item.getQuantity());
            r.setPointsCost(item.getRewardItem().getPointsCost());
            r.setTotalPoints(
                    item.getQuantity() * item.getRewardItem().getPointsCost()
            );
            return r;
        }).toList();

        response.totalPoints =
                response.items.stream()
                        .mapToInt(RewardCartItemResponse::getTotalPoints)
                        .sum();

        return response;
    }
}
