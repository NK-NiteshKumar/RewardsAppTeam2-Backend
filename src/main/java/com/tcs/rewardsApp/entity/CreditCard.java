package com.tcs.rewardsApp.entity;

import com.tcs.rewardsApp.entity.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "credit_cards",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "card_number")
        }
)
@Getter
@Setter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------
    // CARD DETAILS
    // -------------------------
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    // AES-encrypted CVV
    @Column(nullable = false)
    private String cvv;

    // -------------------------
    // STATUS
    // -------------------------
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    // -------------------------
    // RELATIONSHIP
    // -------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // -------------------------
    // AUDIT
    // -------------------------
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
