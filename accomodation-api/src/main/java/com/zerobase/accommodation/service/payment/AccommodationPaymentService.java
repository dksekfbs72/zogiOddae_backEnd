package com.zerobase.accommodation.service.payment;

import com.zerobase.accommodation.domain.dto.payment.AccommodationPaymentDto;
import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.order.AccommodationCart;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import com.zerobase.accommodation.domain.entity.payment.AccommodationPayment;
import com.zerobase.accommodation.domain.form.payment.AccommodationPaymentForm;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationCartRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationOrderItemRepository;
import com.zerobase.accommodation.domain.repository.payment.AccommodationPaymentRepository;
import com.zerobase.accommodation.domain.type.ErrorCode;
import com.zerobase.accommodation.domain.type.PaymentStatus;
import com.zerobase.accommodation.exception.AccommodationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccommodationPaymentService {

    private final AccommodationPaymentRepository accommodationPaymentRepository;
    private final AccommodationCartRepository accommodationCartRepository;

    private static final String AUTHORIZATION = "KakaoAK 5d569ea19c6b8b53c9342d4d65a394e6"; //카카오페이 api 키
    private static final String CONTENTTYPE = "application/x-www-form-urlencoded;charset=utf-8";

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", AUTHORIZATION);
        headers.add("Content-type", CONTENTTYPE);

        return headers;
    }

    @Transactional
    public AccommodationPaymentDto getPaymentReady(AccommodationPaymentForm form) {
        //결제 준비
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready"; //카카오 APi URL

        //카트에서 정보 가져오기
        AccommodationCart accommodationCart = accommodationCartRepository.findByCustomerId(form.getCustomerId())
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_CART));

        AccommodationPayment accommodationPayment = AccommodationPayment.builder()
            .price(form.getPrice())
            .customerId(form.getCustomerId())
            .accommodationOrderItemId(form.getAccommodationOrderItemId())
            .accommodationId(form.getAccommodationId())
            .status(PaymentStatus.PAYMENT_WAIT)
            .build();

        accommodationPaymentRepository.save(accommodationPayment);

        int vat_amount = form.getPrice()/10;

        String parameter = "cid=TC0ONETIME" // 가맹점 코드 - 테스트용으로 고정
            + "&partner_order_id=" + form.getAccommodationOrderItemId()// 가맹점 주문번호를 상품주문 ID로 사용
            + "&partner_user_id=" + form.getCustomerId() // 가맹점 회원 id
            + "&item_name=" + form.getAccommodationName() // 상품명
            + "&quantity=1" // 상품 수량
            + "&total_amount=" + form.getPrice().toString() // 총 금액
            + "&vat_amount=" + vat_amount  //부가세
            + "&tax_free_amount=0"// 상품 비과세 금액
            + "&approval_url=http://localhost:8080/accommodation/payment/kakaopay/approve?accommodationPaymentId=" +accommodationPayment.getId() // 결제 성공 시
            + "&fail_url=http://localhost:8080/accommodation/payment/kakaopay/fail" // 결제 실패 시
            + "&cancel_url=http://localhost:8080/accommodation/payment/kakaopay/cancel"; // 결제 취소 시

        Map<String, String> map = restTemplate.postForObject(url, new HttpEntity<>(parameter, getHeaders()), Map.class);

        accommodationPayment.setTid(map.get("tid"));
        accommodationPaymentRepository.save(accommodationPayment);

        String approval_url = "http://localhost:8080/accommodation/payment/kakaopay/approve?accommodationPaymentId=" +accommodationPayment.getId();
        return AccommodationPaymentDto.from(accommodationPayment, map.get("next_redirect_pc_url"),approval_url);
    }

    public AccommodationPayment paymentSuccess(String pgtoken, Long accommodationPaymentId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/approve";

        AccommodationPayment accommodationPayment = accommodationPaymentRepository.findById(accommodationPaymentId).orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_PAYMENT));

        String parameter = "cid=TC0ONETIME"
            + "&tid=" + accommodationPayment.getTid()
            + "&partner_order_id=" + accommodationPayment.getAccommodationOrderItemId()
            + "&partner_user_id=" + accommodationPayment.getCustomerId()
            + "&pg_token=" + pgtoken;

        Map<String, String> map = restTemplate.postForObject(url, new HttpEntity<>(parameter, getHeaders()), Map.class);

        log.info(map.toString());

        accommodationPayment.setStatus(PaymentStatus.PAID);
        accommodationPayment.setPaymentToken(pgtoken);

        return accommodationPaymentRepository.save(accommodationPayment);
    }

    public AccommodationPayment paymentCancel(Long accommodationPaymentId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/cancel";
        AccommodationPayment accommodationPayment = accommodationPaymentRepository.findById(accommodationPaymentId).orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_PAYMENT));

        String parameter = "cid=TC0ONETIME"
            + "&tid=" + accommodationPayment.getTid()
            + "&cancel_amount=" + accommodationPayment.getPrice()
            + "&cancel_tax_free_amount=0"
            + "&pg_token=" + accommodationPayment.getPaymentToken();

        Map<String, String> map = restTemplate.postForObject(url, new HttpEntity<>(parameter, getHeaders()), Map.class);

        log.info(map.toString());


        accommodationPayment.setStatus(PaymentStatus.CANCELED);

        return accommodationPaymentRepository.save(accommodationPayment);
    }

    public Page<AccommodationPaymentDto> getAccommodationPayment(Long customerId, Pageable pageable) {
        Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("createAt"));

        Page<AccommodationPayment> accommodationPayments
            = accommodationPaymentRepository.findAllByCustomerIdAndStatus(customerId,PaymentStatus.PAID,limit);

        List<AccommodationPaymentDto> accommodationPaymentDtos = new ArrayList<>();

        for(AccommodationPayment accommodationPayment : accommodationPayments){
            accommodationPaymentDtos.add(AccommodationPaymentDto.from(accommodationPayment));
        }

        return new PageImpl<>(accommodationPaymentDtos, limit, accommodationPayments.getTotalElements());
    }
}
