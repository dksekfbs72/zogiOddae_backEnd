package com.zerobase.accommodation.controller.order;

import com.zerobase.accommodation.domain.dto.accommodation.AccommodationOrderItemDto;
import com.zerobase.accommodation.domain.model.WebResponseData;
import com.zerobase.accommodation.service.order.AccommodationOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/accommodation/order")
@RequiredArgsConstructor
public class AccommodationOrderController {

    private final AccommodationOrderService accommodationOrderService;

    @PostMapping
    public @ResponseBody WebResponseData<String> accommodationOrder(@RequestParam Long accommodationPaymentId, Long customerId) {
//		if (memberDetails==null) {
//			throw new AccommodationException(ErrorCode.NOT_AUTHORIZED);
//		}
        accommodationOrderService.accommodationOrder(customerId, accommodationPaymentId);
        return WebResponseData.ok("예약에 성공하였습니다.");
    }

    @GetMapping
    public @ResponseBody WebResponseData<Page<AccommodationOrderItemDto>> getAccommodationOrder(@RequestParam Long customerId, final Pageable pageable) {
//		if (memberDetails==null) {
//			throw new AccommodationException(ErrorCode.NOT_AUTHORIZED);
//		}
        return WebResponseData.ok(accommodationOrderService.getAccommodationOrder(customerId,pageable));
    }
}
