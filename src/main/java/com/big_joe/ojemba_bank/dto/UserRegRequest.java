package com.big_joe.ojemba_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegRequest {

    private String firstName;
    private String otherName;
    private String lastName;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private BigDecimal accountBalance;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
}
