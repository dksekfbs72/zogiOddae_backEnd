package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.order.LeisureOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureOrderDto {
	private Long orderId;

	private String reservationAt;

	private String reservationId;

	private String customerName;

	private String email;

	private List<LeisureOrderItemListDto> orderItemList;

	public static LeisureOrderDto from(LeisureOrder leisureOrder, List<LeisureOrderItemListDto> leisureOrderItemDtoList) {
		return LeisureOrderDto.builder()
			.reservationAt(leisureOrder.getCreateAt().toString())
			.orderId(leisureOrder.getId())
			.reservationId(leisureOrder.getReservationId())
			.customerName("String")
			.email("String")
			.orderItemList(leisureOrderItemDtoList)
			.build();
	}
}
