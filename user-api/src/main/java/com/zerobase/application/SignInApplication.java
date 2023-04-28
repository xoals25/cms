package com.zerobase.application;

import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.SignInForm;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.model.Customer;
import com.zerobase.exception.CustomException;
import com.zerobase.service.cutomer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;

    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부
        Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        // 2. 토큰을 발행하고

        // 3. 토큰을 response한다.
        return provider.createToken(
                customer.getEmail(),
                customer.getId(),
                UserType.CUSTOMER
        );
    }
}
