package com.zerobase.leisure.service.leisure;

import com.zerobase.leisure.domain.dto.leisure.LeisureDayOffDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureListDto;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureDayOff;
import com.zerobase.leisure.domain.form.LeisureDayOffForm;
import com.zerobase.leisure.domain.form.LeisureForm;
import com.zerobase.leisure.domain.repository.leisure.LeisureDayOffRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.exception.LeisureException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLeisureService {
	private final LeisureRepository leisureRepository;

	public Page<LeisureListDto> getAllLeisure(Long sellerId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

		Page<Leisure> leisurePage = leisureRepository.findAllBySellerId(sellerId, limit);

		List<LeisureListDto> leisureDtos = LeisureListDto.fromList(leisurePage.toList());

		return new PageImpl<>(leisureDtos, limit, leisurePage.getTotalElements());
	}

	public Leisure getDetailLeisure(Long leisureId) {
		return leisureRepository.findById(leisureId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE));
	}
}
