package com.big_joe.ojemba_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private String transactionType;
    private BigDecimal transactionAmt;
    private String senderAccountNumber;
    private String senderAccountName;
    private String recipientAccountNumber;
    private String recipientAccountName;
    private String description;
    private String status;
    private String transactionReference;
}
