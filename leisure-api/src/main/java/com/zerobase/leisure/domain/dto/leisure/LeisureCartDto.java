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
public class LeisureCartDto {
	private Long cartId;

	private List<LeisureCartItemDto> cartItemList;

	private Integer totalPrice;
}
