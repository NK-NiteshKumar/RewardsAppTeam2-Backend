package com.tcs.rewardsApp.entity;

import com.tcs.rewardsApp.entity.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_card", columnList = "card_id"),
                @Index(name = "idx_transaction_status", columnList = "status")
        }
)
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------
    // TRANSACTION DETAILS
    // -------------------------

    @Column(name = "transaction_id", nullable = false, unique = true, length = 12)
    private String transactionId;

    @Column(nullable = false)
    private Integer amount; // multiple of 100

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(length = 100)
    private String merchant; // optional

    // -------------------------
    // STATUS
    // -------------------------
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // -------------------------
    // RELATIONSHIP
    // -------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CreditCard creditCard;

    // -------------------------
    // REWARD INFO
    // -------------------------
    @Column(name = "reward_points")
    private Integer rewardPoints; // populated only when processed

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    // -------------------------
    // AUDIT
    // -------------------------
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
