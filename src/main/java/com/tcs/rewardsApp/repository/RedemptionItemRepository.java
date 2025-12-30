package com.tcs.rewardsApp.repository;

import com.tcs.rewardsApp.entity.RedemptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedemptionItemRepository
        extends JpaRepository<RedemptionItem, Long> {
}
