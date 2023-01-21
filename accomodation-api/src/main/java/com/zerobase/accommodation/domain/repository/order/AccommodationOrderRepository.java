package com.zerobase.accommodation.domain.repository.order;

import com.zerobase.accommodation.domain.entity.order.AccommodationOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationOrderRepository extends JpaRepository<AccommodationOrder, Long> {

    Optional<AccommodationOrder> findByCustomerIdAndAccommodationPaymentId(Long customerId, Long acccommodaiotnPaymentId);

    List<AccommodationOrder> findByCustomerId(Long customerId);
}
