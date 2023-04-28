package com.zerobase.domain.model;

import com.zerobase.domain.product.AddProductItemForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;

	@Audited
	private String name;

	@Audited
	private Integer price;

	private Integer count;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public static ProductItem of(Long sellerId, AddProductItemForm form) {
		return ProductItem.builder()
			.sellerId(sellerId)
			.name(form.getName())
			.price(form.getPrice())
			.count(form.getCount())
			.build();
	}
}
