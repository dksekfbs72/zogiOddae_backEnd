package com.zerobase.leisure.domain.repository.coupon;

import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeisureCouponRepository extends JpaRepository<LeisureCoupon, Long> {

	Optional<LeisureCoupon> findByCustomerIdAndCouponGroupId(Long customerId, Long couponGroupId);

	Long countByCouponGroupId(Long couponGroupId);

	Optional<List<LeisureCoupon>> findByCustomerIdAndUsedYN(Long customerId, boolean usingYn);
}
