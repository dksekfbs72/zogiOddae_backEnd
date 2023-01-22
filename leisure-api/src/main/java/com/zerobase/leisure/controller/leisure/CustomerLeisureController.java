package com.zerobase.leisure.controller.leisure;


import com.zerobase.leisure.domain.dto.leisure.CustomerLeisureDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureListDto;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.leisure.CustomerLeisureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customer/leisure")
@RequiredArgsConstructor
public class CustomerLeisureController {

	private final CustomerLeisureService customerLeisureService;

	@GetMapping
	public @ResponseBody WebResponseData<Page<LeisureListDto>> getLeisure(@RequestParam Long sellerId, final
		Pageable pageable) { //소비자 상품 전체 조회
		Page<LeisureListDto> leisureDtos = customerLeisureService.getAllLeisure(sellerId, pageable);
		return WebResponseData.ok(leisureDtos);
	}

	@GetMapping("/detail")
	public @ResponseBody WebResponseData<CustomerLeisureDto> getDetailLeisure(@RequestParam Long leisureId) {
		return WebResponseData.ok(
			CustomerLeisureDto.from(customerLeisureService.getDetailLeisure(leisureId)));
	}
}
