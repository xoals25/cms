package com.zerobase.client;

import com.zerobase.client.mailgurn.SendMailForm;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping("sandbox5b92e55f28c14ff694bc3ddee6234e2f.mailgun.org/messages")
    Response sendEmail(@SpringQueryMap SendMailForm form);
}
