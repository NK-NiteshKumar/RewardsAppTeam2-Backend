package com.tcs.rewardsApp.service.reward;

import com.tcs.rewardsApp.dto.response.*;
import com.tcs.rewardsApp.entity.Redemption;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.CustomerRepository;
import com.tcs.rewardsApp.repository.RedemptionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class RedemptionHistoryServiceImpl
        implements RedemptionHistoryService {

    private final CustomerRepository customerRepository;
    private final RedemptionRepository redemptionRepository;

    public RedemptionHistoryServiceImpl(
            CustomerRepository customerRepository,
            RedemptionRepository redemptionRepository
    ) {
        this.customerRepository = customerRepository;
        this.redemptionRepository = redemptionRepository;
    }

    @Override
    public Page<RedemptionHistoryResponse> getHistory(
            Long customerId,
            int page,
            int size
    ) {

        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("redeemedDate").descending());

        return redemptionRepository
                .findByCustomerId(customerId, pageable)
                .map(this::map);
    }

    private RedemptionHistoryResponse map(Redemption redemption) {

        RedemptionHistoryResponse r = new RedemptionHistoryResponse();
        r.setRedemptionId(redemption.getId());
        r.setPointsUsed(redemption.getPointsUsed());
        r.setRedeemedDate(redemption.getRedeemedDate());

        r.setItems(
                redemption.getItems().stream().map(i -> {
                    RedemptionItemResponse ir =
                            new RedemptionItemResponse();
                    ir.setRewardItemName(
                            i.getRewardItem().getName()
                    );
                    ir.setQuantity(i.getQuantity());
                    ir.setPointsCost(i.getPointsCost());
                    return ir;
                }).toList()
        );

        return r;
    }
}
