package com.zerobase.leisure.service.leisure;

import com.zerobase.leisure.domain.dto.leisure.LeisureWishListDto;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureWishList;
import com.zerobase.leisure.domain.repository.leisure.LeisureRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureWishListRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.exception.LeisureException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeisureWishService {

	private final LeisureWishListRepository leisureWishListRepository;
	private final LeisureRepository leisureRepository;

	public LeisureWishList addLeisureWish(Long memberId, Long leisureId) {
		leisureRepository.findById(leisureId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE));

		if (leisureWishListRepository.findByMemberIdAndLeisureId(memberId, leisureId)
			.isPresent()) {
			throw new LeisureException(ErrorCode.ALREADY_WISHED_LEISURE);
		}

		return leisureWishListRepository.save(LeisureWishList.builder()
			.memberId(memberId)
			.leisureId(leisureId)
			.build());
	}

	@Transactional
	public void deleteLeisureWish(Long memberId, Long leisureId) {
		leisureWishListRepository.delete(leisureWishListRepository.findByMemberIdAndLeisureId(memberId, leisureId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_WISHED_LEISURE)));
	}

	public Page<LeisureWishListDto> getLeisureWishList(Long memberId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

		Page<LeisureWishList> leisureWishListList = leisureWishListRepository.findAllByMemberId(memberId, limit)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_HAD_WISHLIST));

		List<LeisureWishListDto> leisureWishListDtoList = new ArrayList<>();

		for (LeisureWishList leisureWishList : leisureWishListList.toList()) {
			Leisure leisure = leisureRepository.findById(leisureWishList.getLeisureId())
				.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE));

			leisureWishListDtoList.add(
				LeisureWishListDto.builder()
					.wishListId(leisureWishList.getId())
					.id(leisureWishList.getLeisureId())
					.memberId(memberId)
					.name(leisure.getLeisureName())
					.addr(leisure.getAddr())
					.price(leisure.getPrice())
					.pictureUrl(leisure.getPictureUrl())
					.build()
			);
		}
		return new PageImpl<>(leisureWishListDtoList, limit, leisureWishListList.getTotalElements());
	}
}
