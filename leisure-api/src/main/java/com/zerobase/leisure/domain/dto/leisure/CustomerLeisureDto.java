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
public class CustomerLeisureDto {
	private Long id;
	private Long sellerId;

	private String name;
	private String addr;
	private Integer price;
	private String description;

	private String pictureUrl;

	private Integer minPerson;
	private Integer maxPerson;

	private double lat;
	private double lon;

	public static CustomerLeisureDto from(Leisure leisure) {
		return CustomerLeisureDto.builder()
			.id(leisure.getId())
			.sellerId(leisure.getSellerId())
			.name(leisure.getLeisureName())
			.addr(leisure.getAddr())
			.pictureUrl(leisure.getPictureUrl())
			.price(leisure.getPrice())
			.maxPerson(leisure.getMaxPerson())
			.minPerson(leisure.getMinPerson())
			.description(leisure.getDescription())
			.lat(leisure.getLat())
			.lon(leisure.getLon())
			.build();
	}

	public static List<CustomerLeisureDto> fromList(List<Leisure> leisureList) {
		return leisureList.stream()
			.map(CustomerLeisureDto::from)
			.collect(Collectors.toList());
	}
}
