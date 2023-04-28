package com.zerobase.application;

import com.zerobase.client.MailgunClient;
import com.zerobase.client.mailgurn.SendMailForm;
import com.zerobase.domain.SignUpForm;
import com.zerobase.domain.model.Customer;
import com.zerobase.exception.CustomException;
import com.zerobase.exception.ErrorCode;
import com.zerobase.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;

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
                .text(getVerificationEmailBody(form.getEmail(), form.getName(), code))
                .build());

        signUpCustomerService.ChangeCustomerValidateEmail(customer.getId(), code);

        return "회원가입 성공";
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name,
                                            String code) {
        return new StringBuilder()
                .append("hello ")
                .append(name)
                .append("! Please Click Link for verification.\n\n")
                .append("http://localhost:8081/signup/verify/customer?email=")
                .append(email)
                .append("&code=")
                .append(code)
                .toString();
    }
}
