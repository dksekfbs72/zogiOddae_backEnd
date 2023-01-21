package com.zerobase.accommodation.domain.repository.order;

import com.zerobase.accommodation.domain.entity.order.AccommodationCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationCartRepository extends JpaRepository<AccommodationCart, Long> {

    Optional<AccommodationCart> findByCustomerId(Long customerId);
}
