package com.zerobase.leisure.controller.order;

import com.zerobase.leisure.domain.dto.leisure.LeisureOrderCompleteDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureOrderDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureOrderItemDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureOrderListDto;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.order.LeisureOrderService;
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
@RequestMapping("/customer/leisure/order")
@RequiredArgsConstructor
public class LeisureOrderController {

	private final LeisureOrderService leisureOrderService;
	@PostMapping
	public @ResponseBody WebResponseData<LeisureOrderCompleteDto> LeisureOrder(@RequestParam Long leisurePaymentId, Long customerId) {
//		if (memberDetails==null) {
//			throw new LeisureException(ErrorCode.NOT_AUTHORIZED);
//		}

		return WebResponseData.ok(leisureOrderService.LeisureOrder(customerId, leisurePaymentId));
	}

	@GetMapping
	public @ResponseBody WebResponseData<Page<LeisureOrderListDto>> getLeisureOrder(@RequestParam Long customerId,
		final Pageable pageable) {
//		if (memberDetails==null) {
//			throw new LeisureException(ErrorCode.NOT_AUTHORIZED);
//		}
		return WebResponseData.ok(leisureOrderService.getLeisureOrder(customerId,pageable));
	}

	@GetMapping("/detail")
	public @ResponseBody WebResponseData<LeisureOrderDto> getLeisureOrderDetail(@RequestParam Long orderId) {
//		if (memberDetails==null) {
//			throw new LeisureException(ErrorCode.NOT_AUTHORIZED);
//		}
		return WebResponseData.ok(leisureOrderService.getLeisureOrderDetail(orderId));
	}
}
