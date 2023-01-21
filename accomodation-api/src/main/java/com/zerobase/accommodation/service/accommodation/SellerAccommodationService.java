package com.zerobase.accommodation.service.accommodation;

import com.zerobase.accommodation.domain.dto.accommodation.AccommodationListDto;
import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.accommodation.AccommodationDayOff;
import com.zerobase.accommodation.domain.form.accommodation.AccommodationDayOffForm;
import com.zerobase.accommodation.domain.form.accommodation.AccommodationForm;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationDayOffRepository;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationRepository;
import com.zerobase.accommodation.domain.type.ErrorCode;
import com.zerobase.accommodation.exception.AccommodationException;
import org.springframework.data.domain.Page;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerAccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationDayOffRepository accommodationDayOffRepository;

    public Accommodation addAccommodation(Long sellerId, AccommodationForm form) {
        Accommodation accommodation = Accommodation.of(sellerId, form);
        accommodationRepository.save(accommodation);
        return accommodation;
    }

    public Page<AccommodationListDto> getAllAccommodation(Long sellerId, Pageable pageable) {
        Pageable limit = PageRequest.of(pageable.getPageNumber(), 15);

        Page<Accommodation> accommodationList = accommodationRepository.findAllBySellerId(sellerId, limit)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        List<AccommodationListDto> dtoList = new ArrayList<>();

        for (Accommodation accommodation : accommodationList) {
            dtoList.add(AccommodationListDto.from(accommodation));
        }
        return new PageImpl<>(dtoList, limit, accommodationList.getTotalElements());
    }

    public Accommodation updateAccommodation(Long accommodationId, AccommodationForm form) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        accommodation.setAccommodationName(form.getAccommodationName());
        accommodation.setSellerId(accommodation.getSellerId());
        accommodation.setAddr(form.getAddr());
        accommodation.setDescription(form.getDescription());
        accommodation.setPrice(form.getPrice());
        accommodation.setPictureUrl(form.getPictureUrl());
        accommodation.setMaxPerson(form.getMaxPerson());
        accommodation.setMinPerson(form.getMinPerson());
        accommodation.setLat(form.getLat());
        accommodation.setLon(form.getLon());

        return accommodation;
    }

    public Accommodation getDetailAccommodation(Long accommodationId, Long sellerId) {
        return accommodationRepository.findByIdAndSellerId(accommodationId, sellerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION));
    }

    public void deleteAccommodation(Long accommodationId, Long sellerId) {
        accommodationRepository.findByIdAndSellerId(accommodationId, sellerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        accommodationRepository.deleteById(accommodationId);
    }

    public void addAccommodationDayOff(Long accommodationId, AccommodationDayOffForm form) {
        String dayOffStart = form.getStartDate()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        accommodationDayOffRepository.save(AccommodationDayOff.builder()
            .accommodationId(accommodationId)
            .dayOffYear(dayOffStart.substring(0, 4))
            .startDate(form.getStartDate())
            .endDate(form.getEndDate())
            .build());
    }


    public void deleteAccommodationDayOff(Long accommodationDayOffId) {
        accommodationDayOffRepository.findById(accommodationDayOffId).orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION_DAY_OFF));

        accommodationDayOffRepository.deleteById(accommodationDayOffId);
    }

    public List<AccommodationDayOff> getAccommodationDayOff(Long accommodationId) {
        return accommodationDayOffRepository.findAllByAccommodationId(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION_DAY_OFF));
    }

    public void updateAccommodationDayOff(Long accommodationDayOffId, AccommodationDayOffForm form) {
        AccommodationDayOff accommodationDayOff = accommodationDayOffRepository.findById(accommodationDayOffId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION_DAY_OFF));

        String dayOffStart = form.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        accommodationDayOff.setDayOffYear(dayOffStart.substring(0,4));
        accommodationDayOff.setStartDate(form.getStartDate());
        accommodationDayOff.setEndDate(form.getEndDate());

        accommodationDayOffRepository.save(accommodationDayOff);
    }
}
