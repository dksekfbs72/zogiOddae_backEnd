package com.zerobase.accommodation.service.order;

import com.zerobase.accommodation.domain.dto.accommodation.AccommodationOrderItemDto;
import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.coupon.AccommodationCoupon;
import com.zerobase.accommodation.domain.entity.order.AccommodationCart;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrder;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationRepository;
import com.zerobase.accommodation.domain.repository.coupon.AccommodationCouponRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationCartRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationOrderItemRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationOrderRepository;
import com.zerobase.accommodation.domain.repository.payment.AccommodationPaymentRepository;
import com.zerobase.accommodation.domain.type.ErrorCode;
import com.zerobase.accommodation.domain.type.OrderStatus;
import com.zerobase.accommodation.domain.type.PaymentStatus;
import com.zerobase.accommodation.exception.AccommodationException;
import java.time.LocalDateTime;
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
public class AccommodationOrderService {
    private final AccommodationCartRepository accommodationCartRepository;
    private final AccommodationOrderItemRepository accommodationOrderItemRepository;
    private final AccommodationOrderRepository accommodationOrderRepository;
    private final AccommodationPaymentRepository accommodationPaymentRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationCouponRepository accommodationCouponRepository;

    @Transactional
    public void accommodationOrder(Long customerId, Long accommodationPaymentId) {
        if (accommodationOrderRepository.findByCustomerIdAndAccommodationPaymentId(customerId, accommodationPaymentId).isPresent()) {
            throw new AccommodationException(ErrorCode.ALREADY_ORDERED_PAYMENT);
        }
		if (accommodationPaymentRepository.findById(accommodationPaymentId)
			.orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_PAYMENT))
			.getStatus() != PaymentStatus.PAID) {
			throw new AccommodationException(ErrorCode.NOT_PAYMENT_ORDER);
		}

        AccommodationOrder accommodationOrder = accommodationOrderRepository.save(AccommodationOrder.builder()
            .customerId(customerId)
            .accommodationPaymentId(accommodationPaymentId)
            .totalPrice(accommodationPaymentRepository.findById(accommodationPaymentId)
                .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_PAYMENT))
                .getPrice())
            .orderStatus(OrderStatus.PAYMENT)
            .build());

        AccommodationCart accommodationCart = accommodationCartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_CART));

        List<AccommodationOrderItem> accommodationOrderItemList = accommodationOrderItemRepository
            .findAllByAccommodationCart_CustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ORDER_ITEM));

        for (AccommodationOrderItem accommodationOrderItem : accommodationOrderItemList) {
            accommodationOrderItem.setAccommodationCart(null);
            accommodationOrderItem.setReservationId(
                UUID.randomUUID().toString().replace("-", "").substring(0,15));
            accommodationOrderItem.setAccommodationOrderId(accommodationOrder.getId());

            if (accommodationOrderItem.getCouponId() != null) {
                AccommodationCoupon accommodationCoupon = accommodationCouponRepository.findById(accommodationOrderItem.getCouponId())
                    .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_COUPON));
                accommodationCoupon.setUsedYN(true);
                accommodationCoupon.setUsedTime(LocalDateTime.now());

                accommodationCouponRepository.save(accommodationCoupon);
            }

            accommodationOrderItemRepository.save(accommodationOrderItem);
        }

        accommodationCart.setTotalPrice(0);
        accommodationCartRepository.save(accommodationCart);
    }

    public Page<AccommodationOrderItemDto> getAccommodationOrder(Long customerId, Pageable pageable) {
        Pageable limit = PageRequest.of(pageable.getPageNumber(), 15, Sort.by("id"));

        List<Long> accommodationOrderIds = ids(accommodationOrderRepository.findByCustomerId(customerId));

        Page<AccommodationOrderItem> accommodationOrderItems = accommodationOrderItemRepository.findAllByAccommodationOrderIdIn(accommodationOrderIds,limit);

        List<AccommodationOrderItemDto> accommodationOrderItemDtoList = new ArrayList<>();

        List<Long> accommodationIds = accommodationIds(accommodationOrderItems.toList());

        List<Accommodation> accommodationList = accommodationRepository.findAllById(accommodationIds);

        for (int i=0; i<accommodationOrderItems.toList().size(); i++) {
            accommodationOrderItemDtoList.add(AccommodationOrderItemDto.from(accommodationOrderItems.toList().get(i),
                accommodationList.get(whereAccommodation(accommodationList,accommodationIds.get(i)))));
        }

        return new PageImpl<>(accommodationOrderItemDtoList, limit, accommodationOrderItems.getTotalElements());
    }

    private List<Long> ids(List<AccommodationOrder> accommodationOrderList) {
        List<Long> list = new ArrayList<>();
        for (AccommodationOrder accommodationOrder : accommodationOrderList) {
            list.add(accommodationOrder.getId());
        }
        return list;
    }

    private int whereAccommodation(List<Accommodation> accommodationList, Long id) {
        for (int i=0; i<accommodationList.size(); i++) {
            if (accommodationList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private List<Long> accommodationIds(List<AccommodationOrderItem> accommodationOrderList) {
        List<Long> list = new ArrayList<>();
        for (AccommodationOrderItem accommodationOrder : accommodationOrderList) {
            list.add(accommodationOrder.getAccommodationId());
        }
        return list;
    }
}
