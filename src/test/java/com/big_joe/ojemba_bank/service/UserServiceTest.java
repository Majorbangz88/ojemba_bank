package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.CreditDebitRequest;
import com.big_joe.ojemba_bank.dto.EnquiryRequest;
import com.big_joe.ojemba_bank.dto.UserRegRequest;
import com.big_joe.ojemba_bank.exceptions.UniqueUserException;
import com.big_joe.ojemba_bank.utils.AccountUtil;
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
                "Male", "07033099619", "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));
    }

    private static UserRegRequest getUserRegRequest(String firstname, String otherName,
                                                    String lastname, String stateOfOrigin,
                                                    String address, String email, String gender,
                                                    String phone, String altPhone) {
        UserRegRequest regRequest = new UserRegRequest();
        regRequest.setFirstName(firstname);
        regRequest.setOtherName(otherName);
        regRequest.setLastName(lastname);
        regRequest.setStateOfOrigin(stateOfOrigin);
        regRequest.setAddress(address);
        regRequest.setEmail(email);
        regRequest.setGender(gender);
        regRequest.setPhoneNumber(phone);
        regRequest.setAlternativePhoneNumber(altPhone);
        return regRequest;
    }

    @Test
    public void testForUniqueRegistration() {
        UserRegRequest regRequest = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joellegend8@gmail.com",
                "Male", "07033099619", "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        UserRegRequest regRequest2 = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joellegend8@gmail.com",
                "Male", "07033099619", "07033099619"
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
                "Male", "07033099619", "07033099619"
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
                "Male", "07033099619", "07033099619"
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
                "Male", "07033099619", "07033099619"
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
                "Male", "07033099619", "07033099619"
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

}
