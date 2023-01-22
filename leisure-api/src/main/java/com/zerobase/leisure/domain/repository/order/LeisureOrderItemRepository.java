package com.zerobase.leisure.domain.repository.order;

import com.zerobase.leisure.domain.entity.order.LeisureOrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeisureOrderItemRepository extends JpaRepository<LeisureOrderItem, Long> {
	Optional<List<LeisureOrderItem>> findAllByLeisureCart_CustomerId(Long customerId);

	Optional<LeisureOrderItem> findByLeisureCart_CustomerIdAndLeisureId(Long customerId, Long leisureId);
	void deleteByIdAndLeisureCart_CustomerId(Long id, Long LeisureCartId);

	Optional<List<LeisureOrderItem>> findByLeisureOrderId(Long orderId);

	Page<LeisureOrderItem> findAllByLeisureOrderIdIn(List<Long> ids, Pageable limit);
	Page<LeisureOrderItem> findAllByLeisureOrderId(Long orderId, Pageable limit);

	Optional<LeisureOrderItem> findByCouponId(Long couponId);
}