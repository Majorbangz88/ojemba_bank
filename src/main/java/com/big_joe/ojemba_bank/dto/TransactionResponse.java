package com.big_joe.ojemba_bank.dto;

import com.big_joe.ojemba_bank.data.model.Transactions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String responseCode;
    private String responseMessage;
    private Transactions transaction;
}
