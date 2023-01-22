package com.zerobase.leisure.service.coupon;

import com.zerobase.leisure.domain.dto.coupon.LeisureCouponDto;
import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import com.zerobase.leisure.domain.entity.coupon.LeisureCouponGroup;
import com.zerobase.leisure.domain.repository.coupon.LeisureCouponGroupRepository;
import com.zerobase.leisure.domain.repository.coupon.LeisureCouponRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.exception.LeisureException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeisureCouponService {

	private final LeisureCouponRepository leisureCouponRepository;
	private final LeisureCouponGroupRepository leisureCouponGroupRepository;


	public LeisureCoupon issuedLeisureCoupon(Long customerId, Long couponGroupId) {
		Optional<LeisureCoupon> optionalLeisureCoupon =
			leisureCouponRepository.findByCustomerIdAndCouponGroupId(customerId, couponGroupId);

		if (optionalLeisureCoupon.isPresent()) {
			throw new LeisureException(ErrorCode.ALREADY_ISSUED_COUPON);
		}

		Optional<LeisureCouponGroup> optionalLeisureCouponGroup =
			leisureCouponGroupRepository.findById(couponGroupId);

		if (!optionalLeisureCouponGroup.isPresent()) {
			throw new LeisureException(ErrorCode.NOT_REGISTERED_COUPON_GROUP);
		}

		if (LocalDate.now().isAfter(optionalLeisureCouponGroup.get().getEndTime())) {
			throw new LeisureException(ErrorCode.EXPIRED_COUPON);
		}

		Long countCoupons = leisureCouponRepository.countByCouponGroupId(couponGroupId);

		if (countCoupons >= optionalLeisureCouponGroup.get().getIssuedCount()) {
			throw new LeisureException(ErrorCode.EXCEEDED_COUNT_COUPON);
		}

		return leisureCouponRepository.save(LeisureCoupon.builder()
			.couponGroupId(couponGroupId)
			.usedYN(false)
			.customerId(customerId)
			.endTime(optionalLeisureCouponGroup.get().getEndTime())
			.build());
	}

	public List<LeisureCouponDto> getLeisureAllCoupon(Long customerId) {
		List<LeisureCoupon> leisureCouponList = leisureCouponRepository.findByCustomerIdAndUsedYN(
				customerId, false).orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_COUPON));

		List<LeisureCouponDto> leisureCouponDtoList = new ArrayList<>();

		for (LeisureCoupon i : leisureCouponList) {
			leisureCouponDtoList.add(LeisureCouponDto.from(i,
				leisureCouponGroupRepository.findById(i.getCouponGroupId()).get().getSalePrice()));
		}
		return leisureCouponDtoList;
	}
}
