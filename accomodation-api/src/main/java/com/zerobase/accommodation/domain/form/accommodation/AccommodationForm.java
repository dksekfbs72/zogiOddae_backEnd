package com.zerobase.accommodation.domain.form.accommodation;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccommodationForm {
	private String accommodationName;
	private String addr;
	private Integer price;
	private String pictureUrl;
	private String description;

	private Integer minPerson;
	private Integer maxPerson;

	private double lat;
	private double lon;

	private String checkIn;
	private String checkOut;

	private LocalDate dayOffStartDate;
	private LocalDate dayOffEndDate;
}
