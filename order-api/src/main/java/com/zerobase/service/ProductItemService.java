package com.zerobase.service;

import com.zerobase.domain.model.Product;
import com.zerobase.domain.model.ProductItem;
import com.zerobase.domain.product.AddProductItemForm;
import com.zerobase.domain.product.UpdateProductItemForm;
import com.zerobase.exception.CustomException;
import com.zerobase.repository.ProductItemRepository;
import com.zerobase.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.zerobase.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;

    @Transactional
    public ProductItem getProductItem(Long id) {
        return productItemRepository.getReferenceById(id);
    }

    @Transactional
    public Product addProductItem(Long sellerId, AddProductItemForm form) {
        Product product = productRepository
                .findBySellerIdAndId(sellerId, form.getProductId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        if (product.getProductItems().stream()
                .anyMatch(item -> item.getName().equals(form.getName()))) {
            throw new CustomException(SAME_ITEM_NAME);
        }

        ProductItem productItem = ProductItem.of(sellerId, form);
        product.getProductItems().add(productItem);
        return product;
    }

    @Transactional
    public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form) {
        ProductItem productItem = productItemRepository.findById(form.getId())
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT_ITEM));

        productItem.setName(form.getName());
        productItem.setPrice(form.getPrice());
        productItem.setCount(form.getCount());
        return productItem;
    }

    @Transactional
    public void deleteProductItem(Long sellerId, Long productItemId) {
        ProductItem productItem = productItemRepository.findById(productItemId)
                .filter(pi -> pi.getSellerId().equals(sellerId))
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT_ITEM));

        productItemRepository.delete(productItem);
    }
}
