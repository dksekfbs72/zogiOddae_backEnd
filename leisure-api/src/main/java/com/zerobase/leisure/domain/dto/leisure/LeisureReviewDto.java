package com.zerobase.leisure.domain.dto.leisure;

import com.zerobase.leisure.domain.entity.leisure.LeisureReview;
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
public class LeisureReviewDto {
	private Long reviewId;

	private Long customerId;
	private Long sellerId;
	private Long leisureId;

	private double rating;
	private String description;

	private String reply;

	public static LeisureReviewDto from(LeisureReview leisureReview) {
		return LeisureReviewDto.builder()
			.reviewId(leisureReview.getId())
			.sellerId(leisureReview.getSellerId())
			.customerId(leisureReview.getCustomerId())
			.leisureId(leisureReview.getLeisureId())
			.rating(leisureReview.getRating())
			.description(leisureReview.getDescription())
			.reply(leisureReview.getReply())
			.build();
	}

	public static List<LeisureReviewDto> fromList(List<LeisureReview> leisureReviewList) {
		return leisureReviewList.stream()
			.map(LeisureReviewDto::from)
			.collect(Collectors.toList());
	}
}
