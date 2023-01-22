package com.zerobase.leisure.domain.dto.coupon;

import com.zerobase.leisure.domain.entity.coupon.LeisureCouponGroup;
import com.zerobase.leisure.domain.type.CouponTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureCouponGroupDto {
    private Long couponGroupId;

    private Integer salePrice;
    private CouponTarget couponTarget;
    private Integer issuedCount;
    private String endTime;

    public static LeisureCouponGroupDto from(LeisureCouponGroup leisureCouponGroup) {
        return LeisureCouponGroupDto.builder()
            .couponGroupId(leisureCouponGroup.getId())
            .salePrice(leisureCouponGroup.getSalePrice())
            .couponTarget(leisureCouponGroup.getCouponTarget())
            .issuedCount(leisureCouponGroup.getIssuedCount())
            .endTime(leisureCouponGroup.getEndTime().toString())
            .build();
    }
}
