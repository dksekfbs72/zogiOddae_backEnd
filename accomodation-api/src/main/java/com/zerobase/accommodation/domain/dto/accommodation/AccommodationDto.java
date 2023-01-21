package com.zerobase.accommodation.domain.dto.accommodation;

import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
	private Long id;
	private Long sellerId;

	private String accommodationName;
	private String addr;
	private Integer price;

	private Integer minPerson;
	private Integer maxPerson;

	private String checkIn;
	private String checkOut;

	private double lat;
	private double lon;

	public static AccommodationDto from(Accommodation accommodation) {
		return AccommodationDto.builder()
			.id(accommodation.getId())
			.sellerId(accommodation.getSellerId())
			.accommodationName(accommodation.getAccommodationName())
			.addr(accommodation.getAddr())
			.price(accommodation.getPrice())
			.maxPerson(accommodation.getMaxPerson())
			.minPerson(accommodation.getMinPerson())
			.lat(accommodation.getLat())
			.lon(accommodation.getLon())
			.checkIn(accommodation.getCheckIn())
			.checkOut(accommodation.getCheckOut())
			.build();
	}
}
