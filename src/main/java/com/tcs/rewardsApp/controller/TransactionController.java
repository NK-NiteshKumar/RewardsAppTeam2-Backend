package com.tcs.rewardsApp.controller;

import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.service.transaction.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.tcs.rewardsApp.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/cards/{cardId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public void generateTransactions(
            @PathVariable Long customerId,
            @PathVariable Long cardId,
            Principal principal
    ) {
        transactionService.generateTransactions(
                customerId,
                cardId,
                principal.getName()
        );
    }

    @GetMapping
    public Page<TransactionResponse> getTransactions(
            @PathVariable Long customerId,
            @PathVariable Long cardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        if (size < 1) {
            throw new BusinessException("PAGE_SIZE_MUST_BE_GREATER_THAN_ZERO");
        }

        return transactionService.getTransactionsByCard(
                customerId,
                cardId,
                page,
                size
        );
    }

    @PutMapping("/process-rewards")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processRewards(
            @PathVariable Long customerId,
            @PathVariable Long cardId,
            Principal principal
    ) {
        transactionService.processRewards(
                customerId,
                cardId,
                principal.getName()
        );
    }

}
