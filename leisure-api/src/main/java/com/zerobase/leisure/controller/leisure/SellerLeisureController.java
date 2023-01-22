package com.zerobase.leisure.controller.leisure;


import com.zerobase.leisure.domain.dto.leisure.LeisureDayOffDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureListDto;
import com.zerobase.leisure.domain.form.LeisureDayOffForm;
import com.zerobase.leisure.domain.form.LeisureForm;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.leisure.SellerLeisureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/seller/leisure")
@RequiredArgsConstructor
public class SellerLeisureController {

	private final SellerLeisureService sellerLeisureService;
	@PostMapping
	public @ResponseBody WebResponseData<String> addLeisure(@RequestParam Long sellerId,
											@RequestBody LeisureForm form) {
//		if (memberDetails == null) {
//			throw new LeisureException(ErrorCode.NOT_AUTHORIZED);
//		}
		sellerLeisureService.AddLeisure(sellerId, form);
		return WebResponseData.ok("성공적으로 등록했습니다.");
	}

	@GetMapping
	public @ResponseBody WebResponseData<Page<LeisureListDto>> getLeisure(@RequestParam Long sellerId, final
		Pageable pageable) { //셀러 상품 전체 조회
		Page<LeisureListDto> leisureDtos = sellerLeisureService.getAllLeisure(sellerId, pageable);
		return WebResponseData.ok(leisureDtos);
	}

	@GetMapping("/detail")
	public @ResponseBody WebResponseData<LeisureDto> getDetailLeisure(@RequestParam Long leisureId) {
		return WebResponseData.ok(
			LeisureDto.from(sellerLeisureService.getDetailLeisure(leisureId)));
	}

	@PutMapping
	public @ResponseBody WebResponseData<String> updateLeisure(@RequestParam Long leisureId, @RequestBody LeisureForm form) {
		sellerLeisureService.updateLeisure(leisureId, form);
		return WebResponseData.ok("성공적으로 수정했습니다.");
	}

	@DeleteMapping
	public @ResponseBody WebResponseData<String> deleteLeisure(@RequestParam Long leisureId, Long sellerId) {
		sellerLeisureService.deleteLeisure(leisureId, sellerId);
		return WebResponseData.ok("성공적으로 삭제 되었습니다.");
	}


	//휴일 관련
	@PostMapping("/dayOff")
	public @ResponseBody WebResponseData<String> addLeisureDayOff(@RequestParam Long leisureId, @RequestBody LeisureDayOffForm form) {
		sellerLeisureService.addLeisureDayOff(leisureId, form);
		return WebResponseData.ok("성공적으로 등록 되었습니다.");
	}

	@DeleteMapping("/dayOff")
	public @ResponseBody WebResponseData<String> deleteLeisureDayOff(@RequestParam Long leisureDayOffId) {
		sellerLeisureService.deleteLeisureDayOff(leisureDayOffId);
		return WebResponseData.ok("성공적으로 삭제 되었습니다.");
	}

	@GetMapping("/dayOff")
	public @ResponseBody WebResponseData<Page<LeisureDayOffDto>> getLeisureDayOff(@RequestParam Long leisureId, final Pageable pageable) {
		return WebResponseData.ok(sellerLeisureService.getLeisureDayOff(leisureId, pageable));
	}

	@PutMapping("/dayOff")
	public @ResponseBody WebResponseData<String> updateLeisureDayOff(@RequestParam Long leisureDayOffId,
		@RequestBody LeisureDayOffForm form) {
		sellerLeisureService.updateLeisureDayOff(leisureDayOffId, form);
		return WebResponseData.ok("성공적으로 수정되었습니다.");
	}
}
