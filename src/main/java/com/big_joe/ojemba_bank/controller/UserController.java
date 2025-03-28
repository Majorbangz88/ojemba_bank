package com.big_joe.ojemba_bank.controller;

import com.big_joe.ojemba_bank.data.model.Transactions;
import com.big_joe.ojemba_bank.dto.*;
import com.big_joe.ojemba_bank.service.UserService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
@Tag(name = "User Account management APIs")
public class UserController {

    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 Created"
    )
    @PostMapping(path = "/create")
    public BankResponse createAccount(@RequestBody UserRegRequest regRequest) {
        return userService.createAccount(regRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Check Customer's Account Balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @GetMapping(path = "/enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Customer Name Enquiry",
            description = "Retrieve Customer's Name Using Account Number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @GetMapping(path = "/enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Credit Account",
            description = "Increase Customer's Balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @PostMapping(path = "/credit")
    public BankResponse credit(@RequestBody CreditDebitRequest creditRequest) {
        return userService.creditAccount(creditRequest);
    }

    @Operation(
            summary = "Debit Account",
            description = "Decrease Customer's Balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @PostMapping(path = "/debit")
    public BankResponse debit(@RequestBody CreditDebitRequest debitRequest) {
        return userService.debitAccount(debitRequest);
    }

    @Operation(
            summary = "Debit Account",
            description = "Decrease Customer's Balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @PostMapping(path = "/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.fundsTransfer(transferRequest);
    }

    @Operation(
            summary = "Generate Account Statement",
            description = "Generate Bank Account Statement"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 Ok"
    )
    @GetMapping(path = "/statement")
    public List<Transactions> generateTransactionStatement(@RequestBody TransactionEnquiryReq req) throws DocumentException, FileNotFoundException {
        return userService.generateAccountStatement(req);
    }

}
