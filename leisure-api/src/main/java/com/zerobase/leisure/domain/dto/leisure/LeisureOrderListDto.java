package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.order.LeisureOrder;
import com.zerobase.leisure.domain.entity.order.LeisureOrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureOrderListDto {
	private Long orderId;

	private String reservationId;

	private String name;

	private String pictureUrl;

	private Integer persons;

	private Integer orderCount; //주문 개수

	private String startAt;
	private String endAt;

	public static LeisureOrderListDto from(LeisureOrder leisureOrder, LeisureOrderItem leisureOrderItem, Leisure leisure
		,int cnt) {
		return LeisureOrderListDto.builder()
			.orderId(leisureOrder.getId())
			.reservationId(leisureOrder.getReservationId())
			.name(leisure.getLeisureName())
			.pictureUrl(leisure.getPictureUrl())
			.persons(leisureOrderItem.getPersons())
			.orderCount(cnt)
			.startAt(leisureOrderItem.getStartAt().toString())
			.endAt(leisureOrderItem.getEndAt().toString())
			.build();
	}
}
