package com.zerobase.application;

import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.SignInForm;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.model.Customer;
import com.zerobase.domain.model.Seller;
import com.zerobase.exception.CustomException;
import com.zerobase.service.cutomer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtAuthenticationProvider provider;

    public String customerLoginToken(SignInForm form) {
        Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        return provider.createToken(
                customer.getEmail(),
                customer.getId(),
                UserType.CUSTOMER
        );
    }

    public String sellerLoginToken(SignInForm form) {
        Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        return provider.createToken(
                seller.getEmail(),
                seller.getId(),
                UserType.SELLER
        );
    }
}
