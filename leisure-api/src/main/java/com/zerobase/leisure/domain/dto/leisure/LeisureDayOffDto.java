package com.zerobase.leisure.domain.dto.leisure;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.leisure.domain.entity.leisure.LeisureDayOff;
import java.time.LocalDate;
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
public class LeisureDayOffDto {
	private Long id;
	private Long leisureId;

	private String year;
	private String startDate;
	private String endDate;

	public static LeisureDayOffDto from(LeisureDayOff leisureDayOff) {
		return LeisureDayOffDto.builder()
			.id(leisureDayOff.getId())
			.leisureId(leisureDayOff.getLeisureId())
			.year(leisureDayOff.getYear())
			.startDate(leisureDayOff.getStartDate().toString())
			.endDate(leisureDayOff.getEndDate().toString())
			.build();
	}

	public static List<LeisureDayOffDto> fromList(List<LeisureDayOff> leisureDayOff) {
		return leisureDayOff.stream()
			.map(LeisureDayOffDto::from)
			.collect(Collectors.toList());
	}
}
