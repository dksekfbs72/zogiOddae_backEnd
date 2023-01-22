package com.zerobase.leisure.service.payment;

import com.zerobase.leisure.domain.dto.payment.LeisurePaymentDto;
import com.zerobase.leisure.domain.entity.order.LeisurePayment;
import com.zerobase.leisure.domain.form.payment.LeisurePaymentForm;
import com.zerobase.leisure.domain.repository.order.LeisurePaymentRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.domain.type.PaymentStatus;
import com.zerobase.leisure.exception.LeisureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class LeisurePaymentService {

	private final LeisurePaymentRepository leisurePaymentRepository;

	private static final String AUTHORIZATION = "KakaoAK 5d569ea19c6b8b53c9342d4d65a394e6"; //카카오페이 api 키
	private static final String CONTENTTYPE = "application/x-www-form-urlencoded;charset=utf-8";

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", AUTHORIZATION);
		headers.add("Content-type", CONTENTTYPE);

		return headers;
	}

	@Transactional
	public LeisurePaymentDto getPaymentReady(Long customerId,LeisurePaymentForm form) {
		//결제 준비
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready"; //카카오 APi URL

		//상품 주문에서 상품 주문관련 데이터 가져오기
//        LeisureOrderItem leisureOrderItem = leisureOrderItemRepository.findById(form.getLeisureOrderItemId())
//            .orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));
		LeisurePayment leisurePayment = LeisurePayment.builder()
			.price(form.getPrice())
			.customerId(customerId)
			.leisureOrderItemId(form.getLeisureOrderItemId())
			.leisureId(form.getLeisureId())
			.status(PaymentStatus.PAYMENT_WAIT)
			.build();

		leisurePaymentRepository.save(leisurePayment);

		int vat_amount = form.getPrice() / 10;

		String parameter = "cid=TC0ONETIME" // 가맹점 코드 - 테스트용으로 고정
			+ "&partner_order_id=" + form.getLeisureOrderItemId()// 가맹점 주문번호를 상품주문 ID로 사용
			+ "&partner_user_id=" + customerId // 가맹점 회원 id
			+ "&item_name=상품명" //leisureRepository.findById(leisureOrderItem.getLeisureId())
			//.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE)).getLeisureName()// 상품명
			+ "&quantity=1" // 상품 수량
			+ "&total_amount=" + form.getPrice().toString() // 총 금액
			+ "&vat_amount=" + vat_amount  //부가세
			+ "&tax_free_amount=0" // 상품 비과세 금액
			+ "&approval_url=http://localhost:8081/customer/leisure/payment/kakaopay/approve?leisurePaymentId="
			+ leisurePayment.getId() // 결제 성공 시
			+ "&fail_url=http://localhost:8081/customer/leisure/payment/kakaopay/fail" // 결제 실패 시
			+ "&cancel_url=http://localhost:8081/customer/leisure/payment/kakaopay/cancel"; // 결제 취소 시

		Map<String, String> map = restTemplate.postForObject(url,
			new HttpEntity<>(parameter, getHeaders()), Map.class);

		leisurePayment.setTid(Objects.requireNonNull(map).get("tid"));
		leisurePaymentRepository.save(leisurePayment);

		String approval_url =
			"http://localhost:8081/leisure/payment/kakaopay/approve?leisurePaymentId="
				+ leisurePayment.getId();
		return LeisurePaymentDto.from(leisurePayment, map.get("next_redirect_pc_url"),
			approval_url);
	}

	public LeisurePayment paymentSuccess(String pgtoken, Long leisurePaymentId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";

		LeisurePayment leisurePayment = leisurePaymentRepository.findById(leisurePaymentId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_PAYMENT));

		String parameter = "cid=TC0ONETIME"
			+ "&tid=" + leisurePayment.getTid()
			+ "&partner_order_id=" + leisurePayment.getLeisureOrderItemId()
			+ "&partner_user_id=" + leisurePayment.getCustomerId()
			+ "&pg_token=" + pgtoken;

		Map<String, String> map = restTemplate.postForObject(url,
			new HttpEntity<>(parameter, getHeaders()), Map.class);

		log.info(map.toString());

		leisurePayment.setStatus(PaymentStatus.PAID);
		leisurePayment.setPaymentToken(pgtoken);

		return leisurePaymentRepository.save(leisurePayment);
	}

	public LeisurePayment paymentCancel(Long leisurePaymentId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/cancel";

		LeisurePayment leisurePayment = leisurePaymentRepository.findById(leisurePaymentId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_PAYMENT));

		String parameter = "cid=TC0ONETIME"
			+ "&tid=" + leisurePayment.getTid()
			+ "&cancel_amount=" + leisurePayment.getPrice()
			+ "&cancel_tax_free_amount=0"
			+ "&pg_token=" + leisurePayment.getPaymentToken();

		Map<String, String> map = restTemplate.postForObject(url,
			new HttpEntity<>(parameter, getHeaders()), Map.class);

		log.info(map.toString());

		leisurePayment.setStatus(PaymentStatus.CANCELED);

		return leisurePaymentRepository.save(leisurePayment);
	}

	public Page<LeisurePaymentDto> getLeisurePayment(Long customerId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("createAt"));

		Page<LeisurePayment> leisurePaymentPage
			= leisurePaymentRepository.findAllByCustomerIdAndStatus(customerId,PaymentStatus.PAID,limit);

		List<LeisurePaymentDto> leisurePaymentDtos = new ArrayList<>();

		for(LeisurePayment leisurePayment : leisurePaymentPage){
			leisurePaymentDtos.add(LeisurePaymentDto.from(leisurePayment));
		}

		return new PageImpl<>(leisurePaymentDtos, limit, leisurePaymentPage.getTotalElements());
	}
}
