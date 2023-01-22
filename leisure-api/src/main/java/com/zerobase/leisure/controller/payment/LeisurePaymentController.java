package com.zerobase.leisure.controller.payment;

import com.zerobase.leisure.domain.dto.payment.LeisurePaymentDto;
import com.zerobase.leisure.domain.form.payment.LeisurePaymentForm;
import com.zerobase.leisure.domain.model.WebResponseData;
import com.zerobase.leisure.service.payment.LeisurePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/leisure/payment")
@RequiredArgsConstructor
public class LeisurePaymentController {
    //카카오API는 결제 준비 > 결제 요청 - 결제 준비에서 리턴된 URL로 카카오페이와 연결 > 카카오페이(폰)에서 결제 후 결제 승인 이 순서로 진행됩니다.

    private final LeisurePaymentService leisurePaymentService;

    @PostMapping("/kakaopay")
    public WebResponseData<LeisurePaymentDto> kakaopayReady(@RequestParam Long customerId,
        @RequestBody LeisurePaymentForm form) {
        return WebResponseData.ok(leisurePaymentService.getPaymentReady(customerId, form));
    }


    @GetMapping("/kakaopay/approve")
    public WebResponseData<LeisurePaymentDto> kakaopayApprove(@RequestParam(value = "pg_token") String pgtoken,
        Long leisurePaymentId) {
        return WebResponseData.ok(
            LeisurePaymentDto.from(leisurePaymentService.paymentSuccess(pgtoken, leisurePaymentId)));
    }

    @GetMapping("/kakaopay/cancel")
    public WebResponseData<LeisurePaymentDto> kakaopayCancel(@RequestBody Long leisurePaymentId) {
        return WebResponseData.ok(
            LeisurePaymentDto.from(leisurePaymentService.paymentCancel(leisurePaymentId)));
    }

    @GetMapping("/kakaopay/fail")
    public WebResponseData<String> kakaopayFail() {
        return WebResponseData.ok("결제 요청에 실패했습니다. 다시 시도해주세요.");
    }

    @GetMapping
    public WebResponseData<Page<LeisurePaymentDto>> getLeisurePayment(@RequestParam Long customerId, final Pageable pageable) {
        Page<LeisurePaymentDto> leisurePaymentDtos = leisurePaymentService.getLeisurePayment(customerId,pageable);
        return WebResponseData.ok(leisurePaymentDtos);
    }
}
