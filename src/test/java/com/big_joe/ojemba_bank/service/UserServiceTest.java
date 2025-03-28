package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService.deleteAll();
    }

    @Test
    public void testCreateAccount() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));
    }

    private static UserRegRequest getUserRegRequest(String firstname, String otherName,
                                                    String lastname, String stateOfOrigin,
                                                    String address, String email, String password,
                                                    String gender, String phone, String altPhone) {
        UserRegRequest regRequest = new UserRegRequest();
        regRequest.setFirstName(firstname);
        regRequest.setOtherName(otherName);
        regRequest.setLastName(lastname);
        regRequest.setStateOfOrigin(stateOfOrigin);
        regRequest.setAddress(address);
        regRequest.setEmail(email);
        regRequest.setPassword(password);
        regRequest.setGender(gender);
        regRequest.setPhoneNumber(phone);
        regRequest.setAlternativePhoneNumber(altPhone);
        return regRequest;
    }

    @Test
    public void testForUniqueRegistration() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        UserRegRequest regRequest2 = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response1 = userService.createAccount(regRequest2);

        assertThat(userService.count(), is(1L));
        assertEquals("This Account Exists Already!", response1.getResponseMessage());
        assertEquals("002", response1.getResponseCode());
    }

    @Test
    public void testForBalanceEnquiry() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        EnquiryRequest enquiryRequest = EnquiryRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .build();

        BankResponse response1 = userService.balanceEnquiry(enquiryRequest);

        assertNotNull(response1);
        assertEquals("Account Found!", response1.getResponseMessage());
    }

    @Test
    public void testForNameEnquiry() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        EnquiryRequest enquiryRequest = EnquiryRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .build();

        String customerName = userService.nameEnquiry(enquiryRequest);

        assertEquals("Joel Chimaobi Chukwu", customerName);
    }

    @Test
    public void testForCreditTransaction() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        CreditDebitRequest request = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(10000))
                .build();

        BankResponse response1 = userService.creditAccount(request);

        assertNotNull(response1);
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(response1.getAccountInfo().getAccountBalance()));
    }

    @Test
    public void testForDebitTransaction() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(10000))
                .build();

        BankResponse creditResponse = userService.creditAccount(creditRequest);

        assertNotNull(creditResponse);
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(creditResponse.getAccountInfo().getAccountBalance()));

        CreditDebitRequest debitRequest = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(4000))
                .build();

        BankResponse debitResponse = userService.debitAccount(debitRequest);


        assertNotNull(debitResponse);
        assertEquals(0, BigDecimal.valueOf(6000).compareTo(debitResponse.getAccountInfo().getAccountBalance()));
    }

    @Test
    public void testForZeroCreditTransaction() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        CreditDebitRequest request = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(0))
                .build();

        BankResponse response1 = userService.creditAccount(request);

        String invalidMsg  = "The amount you entered is invalid. Please enter a valid amount";

        assertNotNull(response1);
        assertEquals(invalidMsg, response1.getResponseMessage());
    }

    @Test
    public void testForOverWithdrawalInDebitTransaction() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(10000))
                .build();

        BankResponse creditResponse = userService.creditAccount(creditRequest);

        assertNotNull(creditResponse);
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(creditResponse.getAccountInfo().getAccountBalance()));

        CreditDebitRequest debitRequest = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(40000))
                .build();

        BankResponse debitResponse = userService.debitAccount(debitRequest);


        assertNotNull(debitResponse);
        String insufficientBalance = "Your Account Balance Is Insufficient For the transaction you want to carryout";

        assertEquals(insufficientBalance, debitResponse.getResponseMessage());
    }

    @Test
    public void testForTransferTransaction() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "qwert", "Male", "07033099619",
                "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);
        assertEquals("Account created successfully", response.getResponseMessage());

        UserRegRequest regRequest2 = getUserRegRequest(
                "Nnamdi", "Solomon", "Chukwu",
                "Enugu", "Lagos", "nd.solo@gmail.com",
                "asdfghj", "Male", "08032301425",
                "08032301425"
        );

        BankResponse response2 = userService.createAccount(regRequest2);
        assertEquals("Account created successfully", response2.getResponseMessage());
        assertThat(userService.count(), is(2L));

        CreditDebitRequest creditRequest = CreditDebitRequest.builder()
                .accountNumber(response.getAccountInfo().getAccountNumber())
                .amount(BigDecimal.valueOf(10000))
                .build();

        BankResponse creditResponse = userService.creditAccount(creditRequest);

        assertNotNull(creditResponse);
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(creditResponse.getAccountInfo().getAccountBalance()));

        TransferRequest transferRequest = TransferRequest.builder()
                .sourceAccount(response.getAccountInfo().getAccountNumber())
                .destinationAccount(response2.getAccountInfo().getAccountNumber())
                .transferAmount(BigDecimal.valueOf(5000))
                .description("Payment for food")
                .build();

        BankResponse transferResponse = userService.fundsTransfer(transferRequest);
        assertNotNull(transferResponse);

        assertEquals(0, BigDecimal.valueOf(5000).compareTo(transferResponse.getAccountInfo().getAccountBalance()));
    }

}
