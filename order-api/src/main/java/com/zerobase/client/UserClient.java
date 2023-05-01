package com.zerobase.client;

import com.zerobase.client.user.ChangeBalanceForm;
import com.zerobase.client.user.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-api", url = "${feign.client.url.user-api}")
public interface UserClient {

    @GetMapping("/customer/getInfo")
    ResponseEntity<CustomerDto> getCustomerInfo(
        @RequestHeader(name = "X-AUTH-TOKEN") String token
    );

    @PostMapping("/customer/balance")
    ResponseEntity<Integer> changeBalance(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody ChangeBalanceForm form
    );
}
