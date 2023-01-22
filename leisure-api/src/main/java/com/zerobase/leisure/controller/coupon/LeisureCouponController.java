package com.zerobase.leisure.controller.coupon;

import com.zerobase.leisure.domain.dto.coupon.LeisureCouponDto;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.coupon.LeisureCouponService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/leisure/coupon")
@RequiredArgsConstructor
public class LeisureCouponController {

    private final LeisureCouponService leisureCouponService;

    @PostMapping
    public @ResponseBody WebResponseData<String> issuedLeisureCoupon(@RequestParam Long customerId, Long couponGroupId) {
        leisureCouponService.issuedLeisureCoupon(customerId, couponGroupId);
        return WebResponseData.ok("쿠폰을 발급했습니다.");
    }

    @GetMapping
    public @ResponseBody WebResponseData<List<LeisureCouponDto>> getLeisureAllCoupon(@RequestParam Long customerId) {
        return WebResponseData.ok(leisureCouponService.getLeisureAllCoupon(customerId));
    }
}
