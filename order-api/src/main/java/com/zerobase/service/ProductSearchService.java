package com.zerobase.service;

import com.zerobase.domain.model.Product;
import com.zerobase.exception.CustomException;
import com.zerobase.exception.ErrorCode;
import com.zerobase.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
	private final ProductRepository productRepository;

	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}

	public Product getByProductId(Long productId) {
		return productRepository.findWithProductItemsById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
	}

	public List<Product> getListByProductIds(List<Long> productIds) {
		return productRepository.findAllByIdIn(productIds);
	}
}
