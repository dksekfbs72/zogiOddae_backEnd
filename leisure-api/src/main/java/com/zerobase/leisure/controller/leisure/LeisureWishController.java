package com.zerobase.leisure.controller.leisure;

import com.zerobase.leisure.domain.dto.leisure.LeisureWishListDto;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.leisure.LeisureWishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/leisure/wish")
@RequiredArgsConstructor
public class LeisureWishController {

    private final LeisureWishService leisureWishService;

    @PostMapping
    public @ResponseBody WebResponseData<String> addLeisureWish(@RequestParam Long customerId, @RequestParam Long leisureId) {
        leisureWishService.addLeisureWish(customerId, leisureId);
        return WebResponseData.ok("위시리스트에 성공적으로 등록했습니다.");
    }

    @DeleteMapping
    public @ResponseBody WebResponseData<String> deleteLeisureWish(@RequestParam Long customerId, @RequestParam Long leisureId) {
        leisureWishService.deleteLeisureWish(customerId, leisureId);
        return WebResponseData.ok("성공적으로 삭제 되었습니다.");
    }

    @GetMapping
    public @ResponseBody WebResponseData<Page<LeisureWishListDto>> getLeisureWishList(@RequestParam Long customerId,
        final Pageable pageable) {
        return WebResponseData.ok(leisureWishService.getLeisureWishList(customerId, pageable));
    }
}

