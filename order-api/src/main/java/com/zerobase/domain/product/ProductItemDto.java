package com.zerobase.domain.product;

import com.zerobase.domain.model.ProductItem;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemDto {
	private Long id;

	private String name;

	private Integer price;

	private Integer count;

	public static ProductItemDto from(ProductItem item) {
		return ProductItemDto.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.count(item.getCount())
			.build();
	}
}
