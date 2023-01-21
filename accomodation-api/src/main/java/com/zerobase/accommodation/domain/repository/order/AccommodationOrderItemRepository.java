package com.zerobase.accommodation.domain.repository.order;

import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationOrderItemRepository extends JpaRepository<AccommodationOrderItem, Long> {

    Optional<Object> findByAccommodationCart_CustomerIdAndAccommodationId(Long customerId, Long accommodationId);

    void deleteByIdAndAccommodationCart_CustomerId(Long accommodationOrderItemId, Long customerId);

    Optional<List<AccommodationOrderItem>> findAllByAccommodationCart_CustomerId(Long customerId);

    Optional<Object> findByCouponId(Long id);

    Page<AccommodationOrderItem> findAllByAccommodationOrderIdIn(List<Long> ids, Pageable limit);
}
