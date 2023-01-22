package com.zerobase.leisure.domain.entity.order;

import com.zerobase.leisure.domain.entity.common.BaseEntity;
import com.zerobase.leisure.domain.form.AddLeisureCartForm;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class LeisureOrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;
	private Long leisureId;

	private Long leisureOrderId;

	private Long couponId;
	private Integer salePrice;

	@ManyToOne
	@JoinColumn
	private LeisureCart leisureCart;

	private Integer persons;

	private LocalDateTime startAt;
	private LocalDateTime endAt;

	private Integer price;

	public static LeisureOrderItem of(Long sellerId, Integer price, LeisureCart leisureCart, AddLeisureCartForm form) {
		return LeisureOrderItem.builder()
			.sellerId(sellerId)
			.leisureId(form.getLeisureId())
			.leisureCart(leisureCart)
			.persons(form.getPersons())
			.startAt(form.getStartAt())
			.endAt(form.getEndAt())
			.price(price)
			.salePrice(0)
			.build();
	}
}
