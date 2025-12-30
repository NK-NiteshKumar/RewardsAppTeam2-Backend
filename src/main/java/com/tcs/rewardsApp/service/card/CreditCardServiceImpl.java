package com.tcs.rewardsApp.service.card;

import com.tcs.rewardsApp.dto.request.CreditCardLinkRequest;
import com.tcs.rewardsApp.dto.response.CreditCardResponse;
import com.tcs.rewardsApp.entity.CreditCard;
import com.tcs.rewardsApp.entity.Customer;
import com.tcs.rewardsApp.entity.enums.CardStatus;
import com.tcs.rewardsApp.entity.enums.CustomerStatus;
import com.tcs.rewardsApp.exception.BusinessException;
import com.tcs.rewardsApp.repository.CreditCardRepository;
import com.tcs.rewardsApp.repository.CustomerRepository;
import com.tcs.rewardsApp.utility.AesEncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CustomerRepository customerRepository;

    @Value("${aes.secret.key}")
    private String aesKey;

    public CreditCardServiceImpl(
            CreditCardRepository creditCardRepository,
            CustomerRepository customerRepository
    ) {
        this.creditCardRepository = creditCardRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void linkCard(
            Long customerId,
            CreditCardLinkRequest request,
            String createdBy
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        if (creditCardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new BusinessException("CARD_ALREADY_EXISTS");
        }

        if (!request.getExpiryDate().isAfter(LocalDate.now())) {
            throw new BusinessException("CARD_EXPIRED");
        }

        CreditCard card = new CreditCard();
        card.setCardNumber(request.getCardNumber());
        card.setExpiryDate(request.getExpiryDate());

        // 🔐 AES encryption
        card.setCvv(
                AesEncryptionUtil.encrypt(request.getCvv(), aesKey)
        );

        card.setStatus(CardStatus.LINKED);
        card.setCustomer(customer);

        card.setCreatedBy(createdBy);
        card.setCreatedDate(LocalDateTime.now());

        creditCardRepository.save(card);
    }

    @Override
    public List<CreditCardResponse> getCardsByCustomer(
            Long customerId,
            boolean includeUnlinked
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        List<CreditCard> cards = includeUnlinked
                ? creditCardRepository.findByCustomer(customer)
                : creditCardRepository.findByCustomerAndStatus(customer, CardStatus.LINKED);

        return cards.stream()
                // 🔐 enhancement: expiry enforced everywhere
                .filter(card -> card.getExpiryDate().isAfter(LocalDate.now()))
                .map(this::mapToResponse)
                .toList();
    }


    private CreditCardResponse mapToResponse(CreditCard card) {

        CreditCardResponse response = new CreditCardResponse();
        response.setId(card.getId());
        response.setExpiryDate(card.getExpiryDate());
        response.setStatus(card.getStatus());

        String cardNumber = card.getCardNumber();
        response.setMaskedCardNumber(
                "************" + cardNumber.substring(12)
        );

        return response;
    }

    @Override
    public void unlinkCard(
            Long customerId,
            Long cardId,
            String modifiedBy
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("CARD_NOT_FOUND"));

        // Ensure card belongs to the customer
        if (!card.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("CARD_NOT_LINKED_TO_CUSTOMER");
        }

        if (card.getStatus() == CardStatus.UNLINKED) {
            throw new BusinessException("CARD_ALREADY_UNLINKED");
        }

        card.setStatus(CardStatus.UNLINKED);
        card.setModifiedBy(modifiedBy);
        card.setModifiedDate(LocalDateTime.now());

        creditCardRepository.save(card);
    }

    @Override
    public void relinkCard(
            Long customerId,
            Long cardId,
            String modifiedBy
    ) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("CUSTOMER_NOT_ACTIVE");
        }

        CreditCard card = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("CARD_NOT_FOUND"));

        // Ownership check
        if (!card.getCustomer().getId().equals(customerId)) {
            throw new BusinessException("CARD_NOT_LINKED_TO_CUSTOMER");
        }

        if (card.getStatus() == CardStatus.LINKED) {
            throw new BusinessException("CARD_ALREADY_LINKED");
        }

        // Expiry check
        if (!card.getExpiryDate().isAfter(LocalDate.now())) {
            throw new BusinessException("CARD_EXPIRED");
        }

        card.setStatus(CardStatus.LINKED);
        card.setModifiedBy(modifiedBy);
        card.setModifiedDate(LocalDateTime.now());

        creditCardRepository.save(card);
    }

}
