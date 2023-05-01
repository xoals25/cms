package com.zerobase.controller;

import com.zerobase.application.CartApplication;
import com.zerobase.application.OrderApplication;
import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.redis.AddProductCartForm;
import com.zerobase.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final OrderApplication orderApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody AddProductCartForm form
    ) {
        return ResponseEntity.ok(
            cartApplication.addCart(provider.getuserVo(token).getId(), form));
    }

    @GetMapping
    public ResponseEntity<Cart> showCart(
        @RequestHeader(name = "X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(cartApplication.getCart(provider.getuserVo(token).getId()));
    }

    @PutMapping
    public ResponseEntity<Cart> updateCart(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody Cart cart
    ) {
        return ResponseEntity.ok(
            cartApplication.updateCart(provider.getuserVo(token).getId(), cart)
        );
    }

    @PostMapping("/order")
    public ResponseEntity<Cart> orderCart(
        @RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody Cart cart
    ) {
        orderApplication.order(token, cart);
        return ResponseEntity.ok().build();
    }
}
