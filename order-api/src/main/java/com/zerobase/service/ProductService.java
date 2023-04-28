package com.zerobase.service;

import com.zerobase.domain.model.Product;
import com.zerobase.domain.model.ProductItem;
import com.zerobase.domain.product.AddProductForm;
import com.zerobase.domain.product.UpdateProductForm;
import com.zerobase.domain.product.UpdateProductItemForm;
import com.zerobase.exception.CustomException;
import com.zerobase.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.exception.ErrorCode.NOT_FOUND_PRODUCT;
import static com.zerobase.exception.ErrorCode.NOT_FOUND_PRODUCT_ITEM;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form) {
        return productRepository.save(Product.of(sellerId, form));
    }

    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form) {
        Product product = productRepository
                .findBySellerIdAndId(sellerId, form.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        product.setName(form.getName());
        product.setDescription(form.getDescription());

        for (UpdateProductItemForm itemForm : form.getItems()) {
            ProductItem item = product.getProductItems().stream()
                    .filter(p1 -> p1.getId().equals(itemForm.getId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT_ITEM));

            item.setName(itemForm.getName());
            item.setPrice(itemForm.getPrice());
            item.setCount(itemForm.getCount());
        }

        return product;
    }

    // 삭제할 때 하위 객체까지 같이 삭제 할 수 있는지가 주의해야할 포인트이다.
    @Transactional
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository
                .findBySellerIdAndId(sellerId, productId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        productRepository.delete(product);
    }
}
