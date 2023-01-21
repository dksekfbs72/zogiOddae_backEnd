package com.zerobase.accommodation.domain.repository.coupon;

import com.zerobase.accommodation.domain.entity.coupon.AccommodationCoupon;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationCouponRepository extends JpaRepository<AccommodationCoupon, Long> {

    Optional<AccommodationCoupon> findByCustomerIdAndCouponGroupId(Long customerId, Long couponGroupId);

    Long countByCouponGroupId(Long couponGroupId);

    Page<AccommodationCoupon> findAllByCustomerIdAndUsedYNFalse(Long customerId, Pageable limit);
}
