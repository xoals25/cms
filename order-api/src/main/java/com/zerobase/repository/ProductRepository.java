package com.zerobase.repository;

import com.zerobase.domain.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long id);

	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
	Optional<Product> findBySellerIdAndId(Long sellerId, Long productId);
}
