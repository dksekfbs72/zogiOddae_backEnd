package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.leisure.Leisure;
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

	public static LeisureOrderListDto from(LeisureOrderItem leisureOrderItem, Leisure leisure) {
		return LeisureOrderListDto.builder()
			.name(leisure.getLeisureName())
			.persons(leisureOrderItem.getPersons())
			.startAt(leisureOrderItem.getStartAt().toString())
			.endAt(leisureOrderItem.getEndAt().toString())
			.build();
	}
}
