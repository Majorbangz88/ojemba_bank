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
public class TransferRequest {
    public String description;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal transferAmount;
}
