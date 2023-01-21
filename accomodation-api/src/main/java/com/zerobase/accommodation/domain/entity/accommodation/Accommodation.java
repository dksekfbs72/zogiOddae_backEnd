package com.zerobase.accommodation.domain.entity.accommodation;

import com.zerobase.accommodation.domain.entity.common.BaseEntity;
import com.zerobase.accommodation.domain.form.accommodation.AccommodationForm;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Accommodation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;

	private Long categoryId;

	private String accommodationName;
	private String addr;
	private Integer price;
	private String pictureUrl;

	private String description;

	private Integer minPerson;
	private Integer maxPerson;

	private String checkIn;
	private String checkOut;

	private double lat;
	private double lon;

	public static Accommodation of(Long sellerId, AccommodationForm form) {
		return Accommodation.builder()
			.accommodationName(form.getAccommodationName())
			.sellerId(sellerId)
			.addr(form.getAddr())
			.price(form.getPrice())
			.description(form.getDescription())
			.minPerson(form.getMinPerson())
			.maxPerson(form.getMaxPerson())
			.checkIn(form.getCheckIn())
			.checkOut(form.getCheckOut())
			.build();
	}
}
