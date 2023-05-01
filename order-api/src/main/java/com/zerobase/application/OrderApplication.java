package com.zerobase.application;

import static com.zerobase.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.client.UserClient;
import com.zerobase.client.user.ChangeBalanceForm;
import com.zerobase.client.user.CustomerDto;
import com.zerobase.domain.model.ProductItem;
import com.zerobase.domain.redis.Cart;
import com.zerobase.exception.CustomException;
import com.zerobase.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;

    @Transactional
    public void order(String token, Cart cart) {
        // 1번 : 주문 시 기존 카트 버림.
        // 2번 : 선택주문 : 내가 사지 않은 아이템을 살려야 함
        // -- 여러분이 숙제
        Cart orderCart = cartApplication.refreshCart(cart);

        if (orderCart.getMessages().size() > 0) {
            throw  new CustomException(ORDER_FAIL_CHECK_CART);
        }

        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        Integer totalPrice = getTotalPrice(cart);

        if (customerDto.getBalance() < totalPrice) {
            throw new CustomException(ORDER_FAIL_NO_MONEY);
        }

        // 롤백 계획에 대해서 생각해야 함
        // 주문 하는 순간에 계좌에 돈을 빼서 잔액이 부족한 경우에 롤백 계획을 생각해 줘야 한다.
        userClient.changeBalance(token, ChangeBalanceForm.builder()
            .from("USER")
            .message("Order")
            .money(-totalPrice)
            .build());

        for (Cart.Product product : orderCart.getProducts()) {
            for (Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount() - cartItem.getCount());
            }
        }
    }

    private Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream().flatMapToInt(
            product -> product.getItems().stream().flatMapToInt(
                productItem -> IntStream.of(productItem.getPrice() * productItem.getCount()))).sum();
    }
    // 결제를 위해 필요한 것
    // 1번 : 물건들이 전부 주문 가능한 상태인지 확인
    // 2번 : 가격 변동이 있었는지에 대해 확인
    // 3번 : 고객의 돈이 충분한지.
    // 4번 : 걸제 & 상품의 재고 관리
}
