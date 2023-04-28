package com.zerobase.service;

import com.zerobase.client.MailgunClient;
import com.zerobase.client.mailgurn.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    void emailTest() {
        SendMailForm form = SendMailForm.builder()
                .from("xoals25@naver.com")
                .to("xoals22421@gmail.com")
                .subject("제목")
                .text("내용")
                .build();

        mailgunClient.sendEmail(form);
    }
}
