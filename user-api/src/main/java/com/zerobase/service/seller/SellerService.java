package com.zerobase.service.seller;

import com.zerobase.domain.SignUpForm;
import com.zerobase.domain.model.Seller;
import com.zerobase.domain.repository.SellerRepository;
import com.zerobase.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.exception.ErrorCode.*;
import static com.zerobase.exception.ErrorCode.EXPIRE_CODE;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(Long id, String email) {
        return sellerRepository.findByIdAndEmail(id, email);
    }

    public Optional<Seller> findValidSeller(String email, String pwd) {
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, pwd);
    }

    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    public boolean isEmailExist(String email) {
        return sellerRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (seller.isVerify()) {
            throw new CustomException(ALREDAY_VERIFY);
        } else if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(WRONG_VERIFICATION);
        } else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(EXPIRE_CODE);
        }

        seller.setVerify(true);
    }

    @Transactional
    public LocalDateTime changeSellerValidateEmail(Long sellerId, String verificationCode) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        seller.setVerificationCode(verificationCode);
        seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

        return seller.getVerifyExpiredAt();
    }
}
