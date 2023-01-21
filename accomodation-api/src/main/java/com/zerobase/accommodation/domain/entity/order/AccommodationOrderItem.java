package com.zerobase.accommodation.domain.entity.order;

import com.zerobase.accommodation.domain.entity.common.BaseEntity;
import com.zerobase.accommodation.domain.form.accommodation.AddAccommodationCartForm;
import java.time.LocalDate;
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
public class AccommodationOrderItem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;
	private Long accommodationId;

	private String reservationId;

	private Long accommodationOrderId;

	private Long couponId;
	private Integer salePrice;

	@ManyToOne
	@JoinColumn
	private AccommodationCart accommodationCart;

	private Integer persons;

	private LocalDate startAt;
	private LocalDate endAt;

	private Integer price;

	public static AccommodationOrderItem of(Long sellerId, Integer price, AccommodationCart accommodationCart, AddAccommodationCartForm form) {
		return AccommodationOrderItem.builder()
			.sellerId(sellerId)
			.accommodationId(form.getAccommodationId())
			.accommodationCart(accommodationCart)
			.persons(form.getPersons())
			.startAt(form.getStartAt())
			.endAt(form.getEndAt())
			.price(price)
			.build();
	}

}
