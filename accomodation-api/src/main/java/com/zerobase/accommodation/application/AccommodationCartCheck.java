package com.zerobase.accommodation.application;

import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.order.AccommodationCart;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationOrderItemRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccommodationCartCheck {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationOrderItemRepository accommodationOrderItemRepository;

    @Transactional
    public void cartCheck(AccommodationCart accommodationCart,
        AccommodationOrderItem accommodationOrderItem) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(
            accommodationOrderItem.getAccommodationId());

        if (!optionalAccommodation.isPresent()) {
            accommodationCart.setTotalPrice(
                accommodationCart.getTotalPrice() - accommodationOrderItem.getPrice());
            accommodationOrderItemRepository.deleteById(accommodationOrderItem.getId());
            return;
        }

        Accommodation accommodation = optionalAccommodation.get();
        if (!accommodation.getPrice().equals(accommodationOrderItem.getPrice())) {
            accommodationCart.setTotalPrice(
                accommodationCart.getTotalPrice() - accommodationOrderItem.getPrice()
                    + accommodation.getPrice());
        }
    }

}
