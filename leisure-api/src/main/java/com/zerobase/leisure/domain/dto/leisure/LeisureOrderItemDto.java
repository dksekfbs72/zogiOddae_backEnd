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
public class LeisureOrderItemDto {
	private Long orderItemId;
	private Long sellerId;

	private Long couponId;
	private Integer salePrice;

	private String name;
	private Integer price;

	private String pictureUrl;

	private Integer persons;

	private String startAt;
	private String endAt;

	public static LeisureOrderItemDto from(LeisureOrderItem leisureOrderItem, Leisure leisure) {
		return LeisureOrderItemDto.builder()
			.orderItemId(leisureOrderItem.getId())
			.sellerId(leisure.getSellerId())
			.couponId(leisureOrderItem.getCouponId())
			.salePrice(leisureOrderItem.getSalePrice())
			.pictureUrl(leisure.getPictureUrl())
			.name(leisure.getLeisureName())
			.price(leisureOrderItem.getPrice())
			.persons(leisureOrderItem.getPersons())
			.startAt(leisureOrderItem.getStartAt().toString())
			.endAt(leisureOrderItem.getEndAt().toString())
			.build();
	}
}
