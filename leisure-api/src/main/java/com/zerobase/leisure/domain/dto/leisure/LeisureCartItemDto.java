package com.zerobase.leisure.domain.dto.leisure;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.order.LeisureOrderItem;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureCartItemDto {
	private Long leisureOrderItemId;

	private String leisureName;
	private Integer price;

	private String pictureUrl;

	private Integer persons;

	@JsonFormat
	private LocalDateTime startAt;
	@JsonFormat
	private LocalDateTime endAt;

	public static LeisureCartItemDto from(LeisureOrderItem leisureOrderItem, Leisure leisure) {
		return LeisureCartItemDto.builder()
			.leisureOrderItemId(leisureOrderItem.getId())
			.leisureName(leisure.getLeisureName())
			.price(leisure.getPrice())
			.persons(leisureOrderItem.getPersons())
			.startAt(leisureOrderItem.getStartAt())
			.endAt(leisureOrderItem.getEndAt())
			.build();
	}
}
