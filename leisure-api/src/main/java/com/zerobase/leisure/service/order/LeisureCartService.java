package com.zerobase.leisure.service.order;

import com.zerobase.leisure.application.LeisureCartCheck;
import com.zerobase.leisure.domain.dto.leisure.LeisureCartDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureCartItemDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureCartPaymentDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureOrderItemDto;
import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import com.zerobase.leisure.domain.entity.coupon.LeisureCouponGroup;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureReservationDay;
import com.zerobase.leisure.domain.entity.order.LeisureCart;
import com.zerobase.leisure.domain.entity.order.LeisureOrderItem;
import com.zerobase.leisure.domain.form.AddLeisureCartForm;
import com.zerobase.leisure.domain.repository.coupon.LeisureCouponGroupRepository;
import com.zerobase.leisure.domain.repository.coupon.LeisureCouponRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureReservationDayRepository;
import com.zerobase.leisure.domain.repository.order.LeisureCartRepository;
import com.zerobase.leisure.domain.repository.order.LeisureOrderItemRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.exception.LeisureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeisureCartService {

	private final LeisureRepository leisureRepository;
	private final LeisureCartRepository leisureCartRepository;
	private final LeisureOrderItemRepository leisureOrderItemRepository;
	private final LeisureCartCheck leisureCartCheck;
	private final LeisureCouponRepository leisureCouponRepository;
	private final LeisureCouponGroupRepository leisureCouponGroupRepository;
	private final LeisureReservationDayRepository leisureReservationDayRepository;

	public void addLeisureCart(Long customerId, AddLeisureCartForm form) {
		if (leisureOrderItemRepository.findByLeisureCart_CustomerIdAndLeisureId(customerId,
			form.getLeisureId()).isPresent()) {
			throw new LeisureException(ErrorCode.ALREADY_IN_CART);
		}
		Leisure leisure = leisureRepository.findById(form.getLeisureId())
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_LEISURE));

		if (leisure.getMinPerson() > form.getPersons()
			|| form.getPersons() > leisure.getMaxPerson()) {
			throw new LeisureException(ErrorCode.TOO_MANY_PERSON);
		}
		Optional<LeisureCart> optionalLeisureCart = leisureCartRepository.findByCustomerId(
			customerId);

		if (leisureReservationDayRepository.findByLeisureIdAndStartAtBetween(form.getLeisureId(), form.getStartAt(),form.getEndAt())
			.isPresent()) throw new LeisureException(ErrorCode.ALREADY_RESERVATION_DAY);
		if (leisureReservationDayRepository.findByLeisureIdAndEndAtBetween(form.getLeisureId(), form.getStartAt(),form.getEndAt())
			.isPresent()) throw new LeisureException(ErrorCode.ALREADY_RESERVATION_DAY);

		LeisureCart leisureCart= optionalLeisureCart.orElseGet(
			() -> leisureCartRepository.save(LeisureCart.builder()
				.customerId(customerId)
				.totalPrice(leisure.getPrice())
				.build()));

		leisureOrderItemRepository.save(
			LeisureOrderItem.of(leisure.getSellerId(), leisure.getPrice(), leisureCart, form));
	}

	@Transactional
	public void deleteLeisureCart(Long customerId, Long leisureOrderItemId) {
		LeisureCart leisureCart = leisureCartRepository.findByCustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_CART));

		LeisureOrderItem leisureOrderItem = leisureOrderItemRepository.findById(
				leisureOrderItemId).orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));

		if (leisureOrderItem.getCouponId() != null) {
			LeisureCoupon leisureCoupon =
				leisureCouponRepository.findById(leisureOrderItem.getCouponId()).get();
			leisureCoupon.setUsedYN(false);
			leisureCouponRepository.save(leisureCoupon);
		}

		leisureOrderItemRepository.deleteByIdAndLeisureCart_CustomerId(leisureOrderItemId,
			customerId);
	}

	@Transactional
	public LeisureCartDto getLeisureCart(Long customerId) {

		LeisureCart leisureCart = leisureCartRepository.findByCustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_CART));

		List<LeisureOrderItem> leisureOrderItemList = leisureOrderItemRepository
			.findAllByLeisureCart_CustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_HAD_ORDER_ITEM));

		for (LeisureOrderItem i : leisureOrderItemList) {
			leisureCartCheck.cartCheck(leisureCart, i);
		}

		leisureOrderItemList = leisureOrderItemRepository.findAllByLeisureCart_CustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_HAD_ORDER_ITEM));

		leisureCart.setTotalPrice(totalPrice(leisureOrderItemList));

		List<Leisure> leisureList = leisureRepository.findAllById(leisureIds(leisureOrderItemList));
		int totalPrice=0;
		List<LeisureCartItemDto> list = new ArrayList<>();
		for (int i=0; i<leisureList.size(); i++) {
			list.add(LeisureCartItemDto.from(leisureOrderItemList.get(i),leisureList.get(i)));
			totalPrice += leisureList.get(i).getPrice();
		}

		return LeisureCartDto.builder()
			.cartId(leisureCart.getId())
			.cartItemList(list)
			.totalPrice(totalPrice)
			.build();
	}

	@Transactional
	public LeisureCartPaymentDto getLeisureCartPayment(Long customerId, String userName) {

		LeisureCart leisureCart = leisureCartRepository.findByCustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_CART));

		List<LeisureOrderItem> leisureOrderItemList = leisureOrderItemRepository
			.findAllByLeisureCart_CustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_HAD_ORDER_ITEM));

		for (LeisureOrderItem i : leisureOrderItemList) {
			leisureCartCheck.cartCheck(leisureCart, i);
		}

		leisureOrderItemList = leisureOrderItemRepository.findAllByLeisureCart_CustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_HAD_ORDER_ITEM));

		leisureCart.setTotalPrice(totalPrice(leisureOrderItemList));

		List<Leisure> leisureList = leisureRepository.findAllById(leisureIds(leisureOrderItemList));

		List<LeisureOrderItemDto> list = new ArrayList<>();
		for (int i=0; i<leisureList.size(); i++) {
			list.add(LeisureOrderItemDto.from(leisureOrderItemList.get(i),leisureList.get(i)));
		}

		return LeisureCartPaymentDto.builder()
			.customerName(userName)
			.cartId(leisureCart.getId())
			.orderItemList(list)
			.totalPrice(leisureCart.getTotalPrice())
			.build();
	}

	private List<Long> leisureIds(List<LeisureOrderItem> leisureOrderItemList) {
		List<Long> list = new ArrayList<>();
		for (LeisureOrderItem i : leisureOrderItemList) {
			list.add(i.getLeisureId());
		}
		return list;
	}

	public void useCoupon(Long customerId, Long leisureOrderItemId, Long couponGroupId) {
		LeisureCouponGroup leisureCouponGroup = leisureCouponGroupRepository.findById(couponGroupId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_COUPON));

		LeisureCoupon leisureCoupon = leisureCouponRepository.findByCustomerIdAndCouponGroupId(customerId,
				couponGroupId).orElseThrow(() -> new LeisureException(ErrorCode.NOT_MY_COUPON));

		if (leisureCoupon.isUsedYN()) {
			throw new LeisureException(ErrorCode.ALREADY_USED_COUPON);
		}

		LeisureOrderItem leisureOrderItem = leisureOrderItemRepository.findById(leisureOrderItemId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));

		leisureOrderItem.setCouponId(leisureCoupon.getId());
		leisureOrderItem.setSalePrice(leisureCouponGroup.getSalePrice());
		leisureOrderItem.setPrice(leisureOrderItem.getPrice()-leisureOrderItem.getSalePrice());

		leisureCoupon.setUsedYN(true);

		leisureCouponRepository.save(leisureCoupon);

		leisureOrderItemRepository.save(leisureOrderItem);
	}

	public void deleteCoupon(Long leisureOrderItemId) {
		LeisureOrderItem leisureOrderItem = leisureOrderItemRepository.findById(leisureOrderItemId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));

		leisureOrderItem.setCouponId(null);
		leisureOrderItem.setPrice(leisureOrderItem.getPrice()+leisureOrderItem.getSalePrice());
		leisureOrderItem.setSalePrice(0);

		leisureOrderItemRepository.save(leisureOrderItem);
	}


	private Integer totalPrice(List<LeisureOrderItem> leisureOrderItemList) {
		Integer total=0;
		for (LeisureOrderItem i : leisureOrderItemList) {
			total += i.getPrice();
		}
		return total;
	}
}
