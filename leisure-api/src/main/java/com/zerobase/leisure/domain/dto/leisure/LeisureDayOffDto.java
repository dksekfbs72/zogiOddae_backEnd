package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.leisure.LeisureDayOff;
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
	private Long dayOffId;
	private Long id;

	private String year;
	private String startDate;
	private String endDate;

	public static LeisureDayOffDto from(LeisureDayOff leisureDayOff) {
		return LeisureDayOffDto.builder()
			.dayOffId(leisureDayOff.getId())
			.id(leisureDayOff.getLeisureId())
			.year(leisureDayOff.getYear())
			.startDate(leisureDayOff.getStartAt().toString())
			.endDate(leisureDayOff.getEndAt().toString())
			.build();
	}

	public static List<LeisureDayOffDto> fromList(List<LeisureDayOff> leisureDayOff) {
		return leisureDayOff.stream()
			.map(LeisureDayOffDto::from)
			.collect(Collectors.toList());
	}
}
