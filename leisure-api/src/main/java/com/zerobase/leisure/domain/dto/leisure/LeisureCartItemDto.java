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
public class LeisureCartItemDto {
	private Long orderItemId;

	private String name;
	private Integer price;

	private String pictureUrl;

	private Integer persons;

	private String startAt;
	private String endAt;

	public static LeisureCartItemDto from(LeisureOrderItem leisureOrderItem, Leisure leisure) {
		return LeisureCartItemDto.builder()
			.orderItemId(leisureOrderItem.getId())
			.name(leisure.getLeisureName())
			.price(leisure.getPrice())
			.pictureUrl(leisure.getPictureUrl())
			.persons(leisureOrderItem.getPersons())
			.startAt(leisureOrderItem.getStartAt().toString())
			.endAt(leisureOrderItem.getEndAt().toString())
			.build();
	}
}
