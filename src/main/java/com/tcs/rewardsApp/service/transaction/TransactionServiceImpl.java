package com.tcs.rewardsApp.service.transaction;

import com.tcs.rewardsApp.entity.CreditCard;
import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.Transaction;
import com.tcs.rewardsApp.entity.enums.CardStatus;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.entity.enums.TransactionStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.CreditCardRepository;
import com.tcs.rewardsApp.repository.CustomerRepository;
import com.tcs.rewardsApp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.tcs.rewardsApp.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CustomerRepository customerRepository;
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;

    private static final int TRANSACTION_COUNT = 100;
    private static final int MIN_AMOUNT = 500;
    private static final int MAX_AMOUNT = 50_000;

    public TransactionServiceImpl(
            CustomerRepository customerRepository,
            CreditCardRepository creditCardRepository,
            TransactionRepository transactionRepository
    ) {
        this.customerRepository = customerRepository;
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void generateTransactions(
            Long customerId,
            Long cardId,
            String createdBy
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("CARD_NOT_FOUND"));

        if (!card.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("CARD_NOT_LINKED_TO_CUSTOMER");
        }

        if (card.getStatus() != CardStatus.LINKED) {
            throw new BusinessException("CARD_NOT_LINKED");
        }

        if (!card.getExpiryDate().isAfter(LocalDate.now())) {
            throw new BusinessException("CARD_EXPIRED");
        }

        Random random = new Random();
        Set<String> generatedIds = new HashSet<>();

        for (int i = 0; i < TRANSACTION_COUNT; i++) {

            Transaction transaction = new Transaction();
            transaction.setTransactionId(
                    generateUniqueTransactionId(generatedIds)
            );

            transaction.setAmount(generateRandomAmount(random));
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setMerchant("DEFAULT_MERCHANT");

            transaction.setStatus(TransactionStatus.UNPROCESSED);
            transaction.setCreditCard(card);

            transaction.setCreatedBy(createdBy);
            transaction.setCreatedDate(LocalDateTime.now());

            transactionRepository.save(transaction);
        }
    }

    private int generateRandomAmount(Random random) {
        int range = (MAX_AMOUNT - MIN_AMOUNT) / 100;
        return (random.nextInt(range + 1) * 100) + MIN_AMOUNT;
    }

    private String generateUniqueTransactionId(Set<String> generatedIds) {

        String txnId;
        do {
            long number =
                    100_000_000_000L +   // smallest 12-digit number
                            (long) (Math.random() * 900_000_000_000L);

            txnId = String.valueOf(number);

        } while (
                generatedIds.contains(txnId) ||
                        transactionRepository.existsByTransactionId(txnId)
        );

        generatedIds.add(txnId);
        return txnId;
    }

    @Override
    public Page<TransactionResponse> getTransactionsByCard(
            Long customerId,
            Long cardId,
            int page,
            int size
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("CARD_NOT_FOUND"));

        if (!card.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("CARD_NOT_LINKED_TO_CUSTOMER");
        }

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by("transactionDate").descending()
        );

        return transactionRepository
                .findByCreditCard(card, pageable)
                .map(this::mapToResponse);
    }

    private TransactionResponse mapToResponse(Transaction txn) {

        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(txn.getTransactionId());
        response.setAmount(txn.getAmount());
        response.setTransactionDate(txn.getTransactionDate());
        response.setStatus(txn.getStatus());
        response.setRewardPoints(txn.getRewardPoints());
        return response;
    }

    @Override
    public void processRewards(
            Long customerId,
            Long cardId,
            String processedBy
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("CARD_NOT_FOUND"));

        if (!card.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("CARD_NOT_LINKED_TO_CUSTOMER");
        }

        if (card.getStatus() != CardStatus.LINKED) {
            throw new BusinessException("CARD_NOT_LINKED");
        }

        if (!card.getExpiryDate().isAfter(LocalDate.now())) {
            throw new BusinessException("CARD_EXPIRED");
        }

        List<Transaction> unprocessedTransactions =
                transactionRepository.findByCreditCardAndStatus(
                        card,
                        TransactionStatus.UNPROCESSED
                );

        if (unprocessedTransactions.isEmpty()) {
            throw new BusinessException("NO_UNPROCESSED_TRANSACTIONS");
        }

        boolean isPremium =
                customer.getCustomerType().name().equals("PREMIUM");

        for (Transaction txn : unprocessedTransactions) {

            int rewardPoints = isPremium
                    ? txn.getAmount() * 10 / 100
                    : txn.getAmount() * 5 / 100;

            txn.setRewardPoints(rewardPoints);
            txn.setStatus(TransactionStatus.PROCESSED);
            txn.setProcessedDate(LocalDateTime.now());
        }

        transactionRepository.saveAll(unprocessedTransactions);
    }

}
