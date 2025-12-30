package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.RewardCart;
import com.tcs.rewardsApp.entity.RewardCartItem;
import com.tcs.rewardsApp.entity.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewardCartItemRepository extends JpaRepository<RewardCartItem, Long> {

    List<RewardCartItem> findByCart(RewardCart cart);

    Optional<RewardCartItem> findByCartAndRewardItem(
            RewardCart cart,
            RewardItem rewardItem
    );

    void deleteByCart(RewardCart cart);
}
