package com.big_joe.ojemba_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionEnquiryReq {
    private String transactionReference;
    private String accountNumber;
    private String startDate;
    private String endDate;
}
