package com.zerobase.controller;

import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.customer.ChangeBalanceForm;
import com.zerobase.domain.customer.CustomerDto;
import com.zerobase.domain.model.Customer;
import com.zerobase.exception.CustomException;
import com.zerobase.service.cutomer.CustomerBalanceService;
import com.zerobase.service.cutomer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.exception.ErrorCode.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    // test

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(
            @RequestHeader(name = "X-AUTH-TOKEN") String token
    ) {
        UserVo vo = provider.getuserVo(token);

        Customer customer = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return ResponseEntity.ok(CustomerDto.from(customer));
    }

    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(
            @RequestHeader(name = "X-AUTH-TOKEN") String token,
            @RequestBody ChangeBalanceForm form
    ) {
        UserVo vo = provider.getuserVo(token);

        return ResponseEntity.ok().body(customerBalanceService
                        .changeBalance(vo.getId(), form)
                        .getCurrentMoney()
        );
    }
}