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
public class LeisureOrderItemListDto {
	private Long orderItemId;
	private Long sellerId;

	private String name;

	private String pictureUrl;

	private Integer persons;

	private String startAt;
	private String endAt;

	public static LeisureOrderItemListDto from(LeisureOrderItem leisureOrderItem, Leisure leisure) {
		return LeisureOrderItemListDto.builder()
			.orderItemId(leisureOrderItem.getId())
			.sellerId(leisure.getSellerId())
			.pictureUrl(leisure.getPictureUrl())
			.name(leisure.getLeisureName())
			.persons(leisureOrderItem.getPersons())
			.startAt(leisureOrderItem.getStartAt().toString())
			.endAt(leisureOrderItem.getEndAt().toString())
			.build();
	}
}
