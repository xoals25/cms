package com.zerobase.application;

import com.zerobase.client.MailgunClient;
import com.zerobase.client.mailgurn.SendMailForm;
import com.zerobase.domain.SignUpForm;
import com.zerobase.domain.model.Customer;
import com.zerobase.domain.model.Seller;
import com.zerobase.exception.CustomException;
import com.zerobase.exception.ErrorCode;
import com.zerobase.service.cutomer.SignUpCustomerService;
import com.zerobase.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SellerService sellerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        Customer customer = signUpCustomerService.signUp(form);

        String code = getRandomCode();

        mailgunClient.sendEmail(SendMailForm.builder()
                .from("tester@test.com")
                .to(form.getEmail())
                .subject("Verification Email!")
                .text(getVerificationEmailBody(form.getEmail(), form.getName(), "customer", code))
                .build());

        signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);

        return "회원가입 성공";
    }

    public void sellerVerify(String email, String code) {
        sellerService.verifyEmail(email, code);
    }

    public String sellerSignUp(SignUpForm form) {
        if (sellerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        Seller seller = sellerService.signUp(form);

        String code = getRandomCode();

        mailgunClient.sendEmail(SendMailForm.builder()
                .from("tester@test.com")
                .to(form.getEmail())
                .subject("Verification Email!")
                .text(getVerificationEmailBody(form.getEmail(), form.getName(), "seller", code))
                .build());

        sellerService.changeSellerValidateEmail(seller.getId(), code);

        return "회원가입 성공";
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(
            String email,
            String name,
            String type,
            String code
    ) {
        return new StringBuilder()
                .append("hello ")
                .append(name)
                .append("! Please Click Link for verification.\n\n")
                .append("http://localhost:8081/signup/")
                .append(type)
                .append("/verify?email=")
                .append(email)
                .append("&code=")
                .append(code)
                .toString();
    }
}
