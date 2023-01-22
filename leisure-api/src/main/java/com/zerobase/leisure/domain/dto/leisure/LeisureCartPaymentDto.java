package com.zerobase.leisure.domain.dto.leisure;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureCartPaymentDto {
	private Long cartId;

	private String customerName;

	private List<LeisureOrderItemDto> orderItemList;

	private Integer totalPrice;
}
