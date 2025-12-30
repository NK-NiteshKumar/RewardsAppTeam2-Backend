package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.RewardItem;
import com.tcs.rewardsApp.entity.enums.RewardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardItemRepository extends JpaRepository<RewardItem, Long> {

    List<RewardItem> findByActiveTrue();

    List<RewardItem> findByCategoryAndActiveTrue(RewardCategory category);
}
