package com.zerobase.accommodation.domain.dto.accommodation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationCartDto {
    private Long cartId;

    private List<AccommodationOrderItemDto> accommodationOrderItemList;

    private Integer totalPrice;
}
