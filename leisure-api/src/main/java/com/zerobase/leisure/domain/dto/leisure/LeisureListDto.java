package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.leisure.Leisure;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisureListDto {
	private Long id;
	private Long sellerId;

	private String name;
	private String addr;
	private Integer price;

	private String pictureUrl;

	public static LeisureListDto from(Leisure leisure) {
		return LeisureListDto.builder()
			.id(leisure.getId())
			.sellerId(leisure.getSellerId())
			.name(leisure.getLeisureName())
			.addr(leisure.getAddr())
			.pictureUrl(leisure.getPictureUrl())
			.price(leisure.getPrice())
			.build();
	}

	public static List<LeisureListDto> fromList(List<Leisure> leisureList) {
		return leisureList.stream()
			.map(LeisureListDto::from)
			.collect(Collectors.toList());
	}
}
