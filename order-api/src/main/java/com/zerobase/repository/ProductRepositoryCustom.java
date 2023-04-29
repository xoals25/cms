package com.zerobase.repository;


import com.zerobase.domain.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
	List<Product> searchByName(String name);
}
