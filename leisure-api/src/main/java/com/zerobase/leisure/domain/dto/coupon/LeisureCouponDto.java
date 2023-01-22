package com.zerobase.leisure.domain.dto.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureCouponDto {

	private Long id;
	private Long couponGroupId;

	private String endTime;

	public static LeisureCouponDto from(LeisureCoupon leisureCoupon) {
		return LeisureCouponDto.builder()
			.id(leisureCoupon.getId())
			.couponGroupId(leisureCoupon.getCouponGroupId())
			.endTime(leisureCoupon.getEndTime().toString())
			.build();
	}
}
