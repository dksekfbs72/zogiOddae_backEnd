package com.zerobase.accommodation.controller.accommodation;

import com.zerobase.accommodation.domain.dto.accommodation.AccommodationWishListDto;
import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.accommodation.AccommodationWishList;
import com.zerobase.accommodation.domain.model.WebResponseData;
import com.zerobase.accommodation.service.accommodation.AccommodationWishService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodation/wish")
@RequiredArgsConstructor
public class AccommodationWishController {

    private final AccommodationWishService accommodationWishService;

    //찜 목록 등록
    @PostMapping
    public WebResponseData<AccommodationWishListDto> addAccommodationWish(@RequestParam Long customerId, @RequestParam Long accommodationId) {
        AccommodationWishList accommodationWishList = accommodationWishService.addAccommodationWish(customerId, accommodationId);
        Accommodation accommodation = accommodationWishService.getAccommodationInfo(accommodationId);
        return WebResponseData.ok(AccommodationWishListDto.from(accommodationWishList, accommodation));
    }

    //찜 목록 리스트
    @GetMapping
    public WebResponseData<List<AccommodationWishListDto>> getAllAccommodationWish(@RequestParam Long customerId) {
        return WebResponseData.ok(accommodationWishService.getAllAccommodationWish(customerId));
    }


    //찜 목록 리스트에서 삭제
    @DeleteMapping
    public WebResponseData<String> deleteAccommodationWish(@RequestParam Long customerId, @RequestParam Long accommodationId) {
        accommodationWishService.deleteAccommodationWish(customerId,accommodationId);
        return WebResponseData.ok("성공적으로 삭제 되었습니다.");
    }
}

