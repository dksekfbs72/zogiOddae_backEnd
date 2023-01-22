package com.zerobase.leisure.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	SUCCESS_CODE("성공적으로 작업을 수행 했습니다."),


	SERVER_INTERVAL_ERROR("서버 내부적 오류입니다."),
	NOT_HAD_REVIEW("등록된 리뷰가 없습니다."),
	REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다."),
	NOT_HAVE_LEISURE("등록된 레저 시설이 없습니다."),
	NOT_FOUND_LEISURE("레저 시설을 찾을 수 없습니다."),
	ALREADY_DENIED_USER("이미 사용이 거부된 유저입니다."),
	NOT_FOUND_USER("회원을 찾을 수 없습니다."),
	NOT_HAD_BLACKLIST("블랙리스트가 없습니다."),
	NOT_HAD_WISHLIST("찜한 상품이 없습니다."),
	ALREADY_ISSUED_COUPON("이미 발급받은 쿠폰입니다"),
	EXPIRED_COUPON("기한 만료된 쿠폰입니다."),
	EXCEEDED_COUNT_COUPON("남은 쿠폰이 없습니다."),
	NOT_REGISTERED_COUPON_GROUP("등록되지 않은 쿠폰입니다."),
	NOT_HAD_DAY_OFF("등록된 휴일이 없습니다."),
	NOT_FOUND_DAY_OFF("등록된 휴일이 없습니다."),
	TOO_MANY_PERSON("인원수를 다시 설정해주세요."),
	NOT_FOUND_CART("장바구니 정보를 찾을 수 없습니다."),
	NOT_FOUND_ORDER_ITEM("상품을 찾을 수 없습니다."),
	NOT_HAD_ORDER_ITEM("장바구니에 등록된 상품이 없습니다."),
	ALREADY_IN_CART("이미 장바구니에 담긴 상품입니다."),
	NOT_FOUND_PAYMENT("결제 정보를 찾을 수 없습니다."),
	NOT_AUTHORIZED("로그인중이 아닙니다,"),
	ALREADY_ORDERED_PAYMENT("이미 예약된 결제 정보입니다."),
	NOT_PAYMENT_ORDER("결제된 주문이 아닙니다."),
	NOT_MY_COUPON("발급 받은 쿠폰이 아닙니다."),
	NOT_FOUND_COUPON("쿠폰 정보를 찾을 수 없습니다."),
	ALREADY_USED_COUPON("이미 사용한 쿠폰입니다."),
	ALREADY_RESERVATION_DAY("이미 예약된 시간입니다.")
	;


	private final String description;
}
