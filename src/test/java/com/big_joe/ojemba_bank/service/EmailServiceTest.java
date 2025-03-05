package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.EmailDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void testForSendingEmailAlerts() {
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Email Test")
                .recipient("joellegend582@gmail.com")
                .messageBody("This is to test that email service works!")
                .build();

        emailService.sendEmailAlert(emailDetails);

        System.out.println("Test Passed!");
    }
}
