package com.zerobase.accommodation.controller.order;

import com.zerobase.accommodation.domain.dto.accommodation.AccommodationCartDto;
import com.zerobase.accommodation.domain.dto.accommodation.AccommodationOrderItemDto;
import com.zerobase.accommodation.domain.form.accommodation.AddAccommodationCartForm;
import com.zerobase.accommodation.domain.model.WebResponseData;
import com.zerobase.accommodation.service.order.AccommodationCartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/accommodation/cart")
@RequiredArgsConstructor
public class AccommodationCartController {
	private final AccommodationCartService accommodationCartService;

	@PostMapping
	public @ResponseBody WebResponseData<String> addAccommodationCart(@RequestParam Long customerId, @RequestBody AddAccommodationCartForm form){
		accommodationCartService.addaccommodationCart(customerId, form);
		return WebResponseData.ok("장바구니에 담았습니다.");
	}

	@DeleteMapping
	public @ResponseBody WebResponseData<String> deleteAccommodationCart(@RequestParam Long customerId, Long accommodationOrderItemId){
		accommodationCartService.deleteAccommodationCart(customerId, accommodationOrderItemId);
		return WebResponseData.ok("장바구니에서 제거했습니다.");
	}

	@GetMapping
	public @ResponseBody WebResponseData<AccommodationCartDto> getAccommodationCart(@RequestParam Long customerId){
		return WebResponseData.ok(accommodationCartService.getAccommodationCart(customerId));
	}

	@PutMapping("/coupon")
	public @ResponseBody WebResponseData<String> useCoupon(@RequestParam Long customerId, Long accommodationOrderItemId, Long couponGroupId){
		accommodationCartService.useCoupon(customerId, accommodationOrderItemId, couponGroupId);
		return WebResponseData.ok("쿠폰을 적용했습니다.");
	}

	@DeleteMapping("/coupon")
	public @ResponseBody WebResponseData<String> useCoupon(@RequestParam Long accommodationOrderItemId){
		accommodationCartService.deleteCoupon(accommodationOrderItemId);
		return WebResponseData.ok("쿠폰 적용을 취소했습니다.");
	}
}
