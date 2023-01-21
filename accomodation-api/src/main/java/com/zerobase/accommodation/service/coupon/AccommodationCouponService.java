package com.zerobase.accommodation.service.coupon;

import com.zerobase.accommodation.domain.dto.coupon.AccommodationCouponDto;
import com.zerobase.accommodation.domain.entity.coupon.AccommodationCoupon;
import com.zerobase.accommodation.domain.entity.coupon.AccommodationCouponGroup;
import com.zerobase.accommodation.domain.form.accommodation.AddAccommodationCouponForm;
import com.zerobase.accommodation.domain.repository.coupon.AccommodationCouponGroupRepository;
import com.zerobase.accommodation.domain.repository.coupon.AccommodationCouponRepository;
import com.zerobase.accommodation.domain.type.ErrorCode;
import com.zerobase.accommodation.exception.AccommodationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationCouponService {

    private final AccommodationCouponRepository accommodationCouponRepository;
    private final AccommodationCouponGroupRepository accommodationCouponGroupRepository;


    public AccommodationCoupon issuedAccommodationCoupon(AddAccommodationCouponForm form) {
        Optional<AccommodationCoupon> optionalAccommodationCoupon =
            accommodationCouponRepository.findByCustomerIdAndCouponGroupId(form.getCustomerId(),form.getCouponGroupId());

        if(optionalAccommodationCoupon.isPresent()){
            throw new AccommodationException(ErrorCode.ALREADY_ISSUSED_COUPON);
        }

        Optional<AccommodationCouponGroup> optionalAccommodationCouponGroup =
            accommodationCouponGroupRepository.findById(form.getCouponGroupId());

        if(!optionalAccommodationCouponGroup.isPresent()){
            throw new AccommodationException(ErrorCode.NOT_REGISTERED_COUPON_GROUP);
        }

        if(LocalDate.now().isAfter(optionalAccommodationCouponGroup.get().getEndTime())){
            throw new AccommodationException(ErrorCode.EXPIRED_COUPON);
        }

        Long countCoupons = accommodationCouponRepository.countByCouponGroupId(form.getCouponGroupId());

        if(countCoupons >= optionalAccommodationCouponGroup.get().getIssusedcount()){
            throw new AccommodationException(ErrorCode.EXCEEDED_COUNT_COUPON);
        }

        return accommodationCouponRepository.save(AccommodationCoupon.builder()
            .couponGroupId(form.getCouponGroupId())
            .usedYN(false)
            .customerId(form.getCustomerId())
            .endTime(optionalAccommodationCouponGroup.get().getEndTime())
            .build());
    }

    public Page<AccommodationCouponDto> getAccommodationAllCoupon(Long customerId, Pageable pageable) {
        Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

        Page<AccommodationCoupon> accommodationCoupons
            = accommodationCouponRepository.findAllByCustomerIdAndUsedYNFalse(customerId,limit);

        List<AccommodationCouponDto> accommodationCouponDtos = new ArrayList<>();

        for(AccommodationCoupon accommodationCoupon : accommodationCoupons){
            accommodationCouponDtos.add(AccommodationCouponDto.from(accommodationCoupon));
        }

        return new PageImpl<>(accommodationCouponDtos, limit, accommodationCoupons.getTotalElements());
    }
}
