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
public class SellerLeisureService {
	private final LeisureRepository leisureRepository;
	private final LeisureDayOffRepository leisureDayOffRepository;

	public Leisure AddLeisure(Long sellerId, LeisureForm form) {
		Leisure leisure = Leisure.of(sellerId, form);
		leisureRepository.save(leisure);

		return leisure;
	}

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

	public Leisure updateLeisure(Long leisureId, LeisureForm form) {
		Leisure leisure = leisureRepository.findById(leisureId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE));

		leisure.setLeisureName(form.getLeisureName());
		leisure.setAddr(form.getAddr());
		leisure.setDescription(form.getDescription());
		leisure.setPrice(form.getPrice());
		leisure.setPictureUrl(form.getPictureUrl());
		leisure.setMaxPerson(form.getMaxPerson());
		leisure.setMinPerson(form.getMinPerson());
		leisure.setLat(form.getLat());
		leisure.setLon(form.getLon());

		leisureRepository.save(leisure);
		return leisure;
	}

	public void deleteLeisure(Long leisureId, Long sellerId) {
		leisureRepository.delete(leisureRepository.getFirstByIdAndSellerId(leisureId,sellerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE)));
	}

	public LeisureDayOff addLeisureDayOff(Long leisureId, LeisureDayOffForm form) {
		String dayOffStart = form.getStartDay().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return leisureDayOffRepository.save(LeisureDayOff.builder()
			.leisureId(leisureId)
			.year(dayOffStart.substring(0, 4))
			.startAt(form.getStartDay())
			.endAt(form.getEndDay())
			.build());
	}

	public void deleteLeisureDayOff(Long leisureDayOffId) {
		leisureDayOffRepository.deleteById(leisureDayOffId);
	}

	public Page<LeisureDayOffDto> getLeisureDayOff(Long leisureId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

		Page<LeisureDayOff> leisureDayOffPage = leisureDayOffRepository.findAllByLeisureId(leisureId, limit)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_DAY_OFF));

		List<LeisureDayOffDto> leisureDtoList = LeisureDayOffDto.fromList(leisureDayOffPage.toList());

		return new PageImpl<>(leisureDtoList, limit, leisureDayOffPage.getTotalElements());
	}

	public void updateLeisureDayOff(Long leisureDayOffId, LeisureDayOffForm form) {
		LeisureDayOff leisureDayOff = leisureDayOffRepository.findById(leisureDayOffId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_DAY_OFF));

		String dayOffStart = form.getStartDay().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		leisureDayOff.setYear(dayOffStart.substring(0,4));
		leisureDayOff.setStartAt(form.getStartDay());
		leisureDayOff.setEndAt(form.getEndDay());

		leisureDayOffRepository.save(leisureDayOff);
	}
}
