package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.UserRegRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        UserRegRequest regRequest = new UserRegRequest();
        regRequest.setFirstName("Joel");
        regRequest.setOtherName("Chimaobi");
        regRequest.setLastName("Chukwu");
        regRequest.setStateOfOrigin("Enugu");
        regRequest.setAddress("Lagos");
        regRequest.setEmail("joel@gmail.com");
        regRequest.setGender("Male");
        regRequest.setPhoneNumber("07033099619");
        regRequest.setAlternativePhoneNumber("07033099619");
//        regRequest.setAccountBalance(BigDecimal.ZERO);

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));
    }

}
