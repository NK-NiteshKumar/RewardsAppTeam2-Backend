package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.RewardItemResponse;
import com.tcs.rewardsApp.entity.RewardItem;
import com.tcs.rewardsApp.repository.RewardItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardItemServiceImpl implements RewardItemService {

    private final RewardItemRepository rewardItemRepository;

    @Override
    public List<RewardItemResponse> getAllRewardItems() {
        return rewardItemRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RewardItemResponse mapToResponse(RewardItem item) {
        RewardItemResponse response = new RewardItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setCategory(item.getCategory());
        response.setPointsCost(item.getPointsCost());
        return response;
    }
}
