package com.zerobase.accommodation.domain.dto.accommodation;

import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationOrderItemDto {
    private Long accommodationOrderItemId;
    private Long sellerId;

    private String reservationId;

    private Long couponId;
    private Integer salePrice;

    private String accommodationName;
    private String addr;
    private Integer price;
    private String pictureUrl;
    private Integer persons;
    private LocalDate startAt;
    private LocalDate endAt;

    public static AccommodationOrderItemDto from(AccommodationOrderItem accommodationOrderItem, Accommodation accommodation) {
        return AccommodationOrderItemDto.builder()
            .accommodationOrderItemId(accommodationOrderItem.getId())
            .reservationId(accommodationOrderItem.getReservationId())
            .sellerId(accommodation.getSellerId())
            .couponId(accommodationOrderItem.getCouponId())
            .salePrice(accommodationOrderItem.getSalePrice())
            .accommodationName(accommodation.getAccommodationName())
            .addr(accommodation.getAddr())
            .price(accommodation.getPrice())
            .price(accommodationOrderItem.getPrice())
            .persons(accommodationOrderItem.getPersons())
            .startAt(accommodationOrderItem.getStartAt())
            .endAt(accommodationOrderItem.getEndAt())
            .build();
    }
}