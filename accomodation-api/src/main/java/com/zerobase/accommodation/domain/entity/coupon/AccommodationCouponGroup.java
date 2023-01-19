package com.zerobase.accommodation.domain.entity.coupon;

import com.zerobase.accommodation.domain.entity.common.BaseEntity;
import com.zerobase.accommodation.domain.form.AddAccommodationCouponGroupForm;
import com.zerobase.accommodation.domain.type.CouponTarget;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class AccommodationCouponGroup extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long salePrice;
	private CouponTarget couponTarget;
	private Integer issusedcount;

	private LocalDateTime endTime;

	public static AccommodationCouponGroup of(AddAccommodationCouponGroupForm form){
		return AccommodationCouponGroup.builder()
			.id(form.getAccommodationCouponGroupid())
			.salePrice(form.getSalePrice())
			.couponTarget(form.getCouponTarget())
			.issusedcount(form.getIssusedcount())
			.endTime(form.getEndTime())
			.build();
	}

}