package com.zerobase.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.domain.model.Product;
import com.zerobase.domain.model.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Product> searchByName(String name) {
		String search = "%" + name + "%";

		QProduct qProduct = QProduct.product;

		return jpaQueryFactory.selectFrom(qProduct)
			.where(qProduct.name.like(search))
			.fetch();
	}

}
