package com.zerobase.leisure.domain.dto.coupon;

import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureCouponDto {

	private Long couponId;
	private Long couponGroupId;

	private String endTime;

	public static LeisureCouponDto from(LeisureCoupon leisureCoupon) {
		return LeisureCouponDto.builder()
			.couponId(leisureCoupon.getId())
			.couponGroupId(leisureCoupon.getCouponGroupId())
			.endTime(leisureCoupon.getEndTime().toString())
			.build();
	}
}
