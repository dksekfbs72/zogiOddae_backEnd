package com.zerobase.accommodation.domain.form.payment;

import com.zerobase.accommodation.domain.type.PaymentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AccommodationPaymentForm {
    private Long customerId;
    private Long accommodationOrderItemId; // 주문 상품 아이디

    private String accommodationName;

    private Long accommodationId;

    private Integer price;

    private String tid; //결제 고유 번호
    private String paymentToken; //결제 승인 인증 토큰

    private PaymentStatus status;

    private LocalDateTime canceledAt;
}
