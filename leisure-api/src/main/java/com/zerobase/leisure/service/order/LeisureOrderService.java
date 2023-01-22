package com.zerobase.leisure.service.order;

import com.zerobase.leisure.domain.dto.leisure.LeisureOrderItemDto;
import com.zerobase.leisure.domain.dto.leisure.LeisureOrderListDto;
import com.zerobase.leisure.domain.entity.coupon.LeisureCoupon;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureReservationDay;
import com.zerobase.leisure.domain.entity.order.LeisureCart;
import com.zerobase.leisure.domain.entity.order.LeisureOrder;
import com.zerobase.leisure.domain.entity.order.LeisureOrderItem;
import com.zerobase.leisure.domain.repository.coupon.LeisureCouponRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureReservationDayRepository;
import com.zerobase.leisure.domain.repository.order.LeisureCartRepository;
import com.zerobase.leisure.domain.repository.order.LeisureOrderItemRepository;
import com.zerobase.leisure.domain.repository.order.LeisureOrderRepository;
import com.zerobase.leisure.domain.repository.order.LeisurePaymentRepository;
import com.zerobase.leisure.domain.type.ErrorCode;
import com.zerobase.leisure.domain.type.OrderStatus;
import com.zerobase.leisure.exception.LeisureException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeisureOrderService {

	private final LeisureCartRepository leisureCartRepository;
	private final LeisureOrderItemRepository leisureOrderItemRepository;
	private final LeisureOrderRepository leisureOrderRepository;
	private final LeisurePaymentRepository leisurePaymentRepository;
	private final LeisureRepository leisureRepository;
	private final LeisureCouponRepository leisureCouponRepository;
	private final LeisureReservationDayRepository leisureReservationDayRepository;

	@Transactional
	public void LeisureOrder(Long customerId, Long leisurePaymentId) {
		if (leisureOrderRepository.findByCustomerIdAndLeisurePaymentId(customerId, leisurePaymentId)
			.isPresent()) {
			throw new LeisureException(ErrorCode.ALREADY_ORDERED_PAYMENT);
		}
//		if (leisurePaymentRepository.findById(leisurePaymentId)
//			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_PAYMENT))
//			.getStatus() != PaymentStatus.PAID) {
//			throw new LeisureException(ErrorCode.NOT_PAYMENT_ORDER);
//		}

		LeisureOrder leisureOrder = leisureOrderRepository.save(LeisureOrder.builder()
			.customerId(customerId)
			.leisurePaymentId(leisurePaymentId)
			.totalPrice(leisurePaymentRepository.findById(leisurePaymentId)
				.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_PAYMENT))
				.getPrice())
			.orderStatus(OrderStatus.PAYMENT)
			.reservationId(UUID.randomUUID().toString().replace("-", "").substring(0, 15))
			.build());

		LeisureCart leisureCart = leisureCartRepository.findByCustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_CART));

		List<LeisureOrderItem> leisureOrderItemList = leisureOrderItemRepository
			.findAllByLeisureCart_CustomerId(customerId)
			.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));

		for (LeisureOrderItem leisureOrderItem : leisureOrderItemList) {
			leisureOrderItem.setLeisureCart(null);

			leisureOrderItem.setLeisureOrderId(leisureOrder.getId());

			if (leisureOrderItem.getCouponId() != null) {
				LeisureCoupon leisureCoupon = leisureCouponRepository.findById(
						leisureOrderItem.getCouponId())
					.orElseThrow(() -> new LeisureException(ErrorCode.NOT_FOUND_COUPON));
				leisureCoupon.setUsedYN(true);
				leisureCoupon.setUsedTime(LocalDateTime.now());

				leisureCouponRepository.save(leisureCoupon);
			}

			leisureReservationDayRepository.save(LeisureReservationDay.builder()
				.leisureId(leisureOrderItem.getLeisureId())
				.year(leisureOrderItem.getStartAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
					.substring(0, 4))
				.startAt(leisureOrderItem.getStartAt())
				.endAt(leisureOrderItem.getEndAt())
				.build());
			leisureOrderItemRepository.save(leisureOrderItem);
		}

		leisureCart.setTotalPrice(0);
		leisureCartRepository.save(leisureCart);
	}

	public Page<LeisureOrderListDto> getLeisureOrder(Long customerId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

		Page<LeisureOrder> leisureOrderItems = leisureOrderRepository.findByCustomerId(customerId, limit);

		List<LeisureOrderListDto> leisureOrderItemDtoList = new ArrayList<>();

		for (int i = 0; i < leisureOrderItems.toList().size(); i++) {
			LeisureOrder leisureOrder = leisureOrderItems.toList().get(i);

			List<LeisureOrderItem> leisureOrderItemList = leisureOrderItemRepository.findByLeisureOrderId(
				leisureOrder.getId()).orElseThrow(()->new LeisureException(ErrorCode.NOT_FOUND_ORDER_ITEM));

			leisureOrderItemDtoList.add(LeisureOrderListDto.from(leisureOrder,
				leisureOrderItemList.get(0), leisureRepository.findById(
					leisureOrderItemList.get(0).getLeisureId()).get(), leisureOrderItemList.size()));
		}

		return new PageImpl<>(leisureOrderItemDtoList, limit, leisureOrderItems.getTotalElements());
	}

	public Page<LeisureOrderItemDto> getLeisureOrderDetail(Long orderId, Pageable pageable) {
		Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

		leisureOrderRepository.findById(orderId).get();

		Page<LeisureOrderItem> leisureOrderItems = leisureOrderItemRepository.findAllByLeisureOrderId(
			orderId, limit);

		List<LeisureOrderItemDto> leisureOrderItemDtoList = new ArrayList<>();

		List<Long> leisureIds = leisureIds(leisureOrderItems.toList());

		List<Leisure> leisureList = leisureRepository.findAllById(leisureIds);

		for (int i = 0; i < leisureOrderItems.toList().size(); i++) {
			int index = whereLeisure(leisureList, leisureIds.get(i));
			if (index == -1) {
				continue;
			}
			leisureOrderItemDtoList.add(LeisureOrderItemDto.from(leisureOrderItems.toList().get(i),
				leisureList.get(index)));
		}

		return new PageImpl<>(leisureOrderItemDtoList, limit, leisureOrderItems.getTotalElements());
	}

	private List<Long> ids(List<LeisureOrder> leisureOrderList) {
		List<Long> list = new ArrayList<>();
		for (LeisureOrder leisureOrder : leisureOrderList) {
			list.add(leisureOrder.getId());
		}
		return list;
	}

	private int whereLeisure(List<Leisure> leisureList, Long id) {
		for (int i = 0; i < leisureList.size(); i++) {
			if (leisureList.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private List<Long> leisureIds(List<LeisureOrderItem> leisureOrderList) {
		List<Long> list = new ArrayList<>();
		for (LeisureOrderItem leisureOrder : leisureOrderList) {
			list.add(leisureOrder.getLeisureId());
		}
		return list;
	}
}
