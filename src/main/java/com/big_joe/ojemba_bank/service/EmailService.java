package com.big_joe.ojemba_bank.service;

import com.big_joe.ojemba_bank.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
