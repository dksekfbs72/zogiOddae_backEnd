package com.zerobase.leisure.domain.dto.leisure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureOrderCompleteDto {
	private String reservationId;
	private Integer price;
}
