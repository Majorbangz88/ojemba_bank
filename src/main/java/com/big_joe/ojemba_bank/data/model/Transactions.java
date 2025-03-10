package com.big_joe.ojemba_bank.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;
    private String transactionType;
    private BigDecimal transactionAmt;
    private String accountNumber;
    private String accountName;
    private String status;
    private String transactionReference;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
