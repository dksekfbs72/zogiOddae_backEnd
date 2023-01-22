package com.zerobase.leisure.domain.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.leisure.domain.entity.order.LeisurePayment;
import com.zerobase.leisure.domain.type.PaymentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeisurePaymentDto {
    private Long paymentId;
    private Long customerId;
    private Long orderItemId; // 주문 상품 아이디

    private Long id;

    private Integer price;

    private String tid; //결제 고유 번호
    private String paymentToken; //결제 승인 인증 토큰

    private PaymentStatus status;

    private String canceledAt;
    private String nextRedirectURL;

    private String approveURL;

    public static LeisurePaymentDto from(LeisurePayment leisurePayment, String nextRedirectURL, String approveURL) {
        return LeisurePaymentDto.builder()
            .paymentId(leisurePayment.getId())
            .customerId(leisurePayment.getCustomerId())
            .orderItemId(leisurePayment.getLeisureOrderItemId())
            .id(leisurePayment.getLeisureId())
            .price(leisurePayment.getPrice())
            .tid(leisurePayment.getTid())
            .paymentToken(leisurePayment.getPaymentToken())
            .status(leisurePayment.getStatus())
            .canceledAt(leisurePayment.getCanceledAt().toString())
            .nextRedirectURL(nextRedirectURL)
            .approveURL(approveURL)
            .build();
    }

    public static LeisurePaymentDto from(LeisurePayment leisurePayment) {
        return LeisurePaymentDto.builder()
            .paymentId(leisurePayment.getId())
            .customerId(leisurePayment.getCustomerId())
            .orderItemId(leisurePayment.getLeisureOrderItemId())
            .id(leisurePayment.getLeisureId())
            .price(leisurePayment.getPrice())
            .tid(leisurePayment.getTid())
            .paymentToken(leisurePayment.getPaymentToken())
            .status(leisurePayment.getStatus())
            .canceledAt(leisurePayment.getCanceledAt().toString())
            .build();
    }
}
