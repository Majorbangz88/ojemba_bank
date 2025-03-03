package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.BankResponse;
import com.big_joe.ojemba_bank.dto.UserRegRequest;
import com.big_joe.ojemba_bank.exceptions.UniqueUserException;
import com.big_joe.ojemba_bank.utils.AccountUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                "Enugu", "Lagos", "joel@gmail.com",
                "Male", "07033099619", "07033099619"
        );

        BankResponse response = userService.createAccount(regRequest);

        assertEquals("Account created successfully", response.getResponseMessage());
        assertThat(userService.count(), is(1L));

        UserRegRequest regRequest2 = getUserRegRequest(
                "Joel", "Chimaobi", "Chukwu",
                "Enugu", "Lagos", "joel@gmail.com",
                "Male", "07033099619", "07033099619"
        );

        BankResponse response1 = userService.createAccount(regRequest2);

        assertThat(userService.count(), is(1L));
        assertEquals("This Account Exists Already!", response1.getResponseMessage());
        assertEquals("002", response1.getResponseCode());
    }

}
