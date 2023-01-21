package com.zerobase.accommodation.service.order;

import com.zerobase.accommodation.application.AccommodationCartCheck;
import com.zerobase.accommodation.domain.dto.accommodation.AccommodationCartDto;
import com.zerobase.accommodation.domain.dto.accommodation.AccommodationOrderItemDto;
import com.zerobase.accommodation.domain.entity.accommodation.Accommodation;
import com.zerobase.accommodation.domain.entity.coupon.AccommodationCoupon;
import com.zerobase.accommodation.domain.entity.coupon.AccommodationCouponGroup;
import com.zerobase.accommodation.domain.entity.order.AccommodationCart;
import com.zerobase.accommodation.domain.entity.order.AccommodationOrderItem;
import com.zerobase.accommodation.domain.form.accommodation.AddAccommodationCartForm;
import com.zerobase.accommodation.domain.repository.accommodation.AccommodationRepository;
import com.zerobase.accommodation.domain.repository.coupon.AccommodationCouponGroupRepository;
import com.zerobase.accommodation.domain.repository.coupon.AccommodationCouponRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationCartRepository;
import com.zerobase.accommodation.domain.repository.order.AccommodationOrderItemRepository;
import com.zerobase.accommodation.domain.type.ErrorCode;
import com.zerobase.accommodation.exception.AccommodationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationCartService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationCartRepository accommodationCartRepository;
    private final AccommodationOrderItemRepository accommodationOrderItemRepository;
    private final AccommodationCartCheck accommodationCartCheck;

    private final AccommodationCouponGroupRepository accommodationCouponGroupRepository;
    private final AccommodationCouponRepository accommodationCouponRepository;

    public void addaccommodationCart(Long customerId, AddAccommodationCartForm form) {
        if (accommodationOrderItemRepository.findByAccommodationCart_CustomerIdAndAccommodationId(customerId,
            form.getAccommodationId()).isPresent()) {
            throw new AccommodationException(ErrorCode.ALREADY_IN_CART);
        }
        Accommodation accommodation = accommodationRepository.findById(form.getAccommodationId())
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        if (accommodation.getMinPerson() > form.getPersons()
            || form.getPersons() > accommodation.getMaxPerson()) {
            throw new AccommodationException(ErrorCode.TOO_MANY_PERSON);
        }
        Optional<AccommodationCart> optionalAccommodationCart = accommodationCartRepository.findByCustomerId(customerId);

        AccommodationCart accommodationCart;
        if (optionalAccommodationCart.isPresent()) {
            accommodationCart = optionalAccommodationCart.get();
            accommodationCart.setTotalPrice(accommodationCart.getTotalPrice() + accommodation.getPrice());
            accommodationCartRepository.save(accommodationCart);
        } else {
            accommodationCart = accommodationCartRepository.save(AccommodationCart.builder()
                .customerId(customerId)
                .totalPrice(accommodation.getPrice())
                .build());
        }
        accommodationOrderItemRepository.save(
            AccommodationOrderItem.of(accommodation.getSellerId(), accommodation.getPrice(), accommodationCart, form));
    }

    @Transactional
    public void deleteAccommodationCart(Long customerId, Long accommodationOrderItemId) {
        AccommodationCart accommodationCart = accommodationCartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_CART));
        accommodationCart.setTotalPrice(accommodationCart.getTotalPrice() - accommodationOrderItemRepository.findById(accommodationOrderItemId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ORDER_ITEM))
            .getPrice());

        accommodationOrderItemRepository.deleteByIdAndAccommodationCart_CustomerId(accommodationOrderItemId, customerId);
    }

    @Transactional
    public AccommodationCartDto getAccommodationCart(Long customerId) {

        AccommodationCart accommodationCart = accommodationCartRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_CART));

        List<AccommodationOrderItem> accommodationOrderItemList = accommodationOrderItemRepository.findAllByAccommodationCart_CustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_HAD_ORDER_ITEM));

        for (AccommodationOrderItem i : accommodationOrderItemList) {
            accommodationCartCheck.cartCheck(accommodationCart, i);
        }

        accommodationOrderItemList = accommodationOrderItemRepository.findAllByAccommodationCart_CustomerId(customerId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_HAD_ORDER_ITEM));

        accommodationCart.setTotalPrice(totalPrice(accommodationOrderItemList));

        List<Accommodation> accommodationList = accommodationRepository.findAllById(
            accommodationIds(accommodationOrderItemList));

        List<AccommodationOrderItemDto> list = new ArrayList<>();
        for (int i=0; i<accommodationList.size(); i++) {
            list.add(AccommodationOrderItemDto.from(accommodationOrderItemList.get(i),accommodationList.get(i)));
        }

        return AccommodationCartDto.builder()
            .cartId(accommodationCart.getId())
            .accommodationOrderItemList(list)
            .totalPrice(accommodationCart.getTotalPrice())
            .build();
    }

    private List<Long> accommodationIds(List<AccommodationOrderItem> accommodationOrderItemList) {
        List<Long> list = new ArrayList<>();
        for (AccommodationOrderItem i : accommodationOrderItemList) {
            list.add(i.getAccommodationId());
        }
        return list;
    }

    public void useCoupon(Long customerId, Long accommodationOrderItemId, Long accommodationCouponGroupId) {
        AccommodationCouponGroup accommodationCouponGroup = accommodationCouponGroupRepository.findById(accommodationCouponGroupId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_COUPON));

        AccommodationCoupon accommodationCoupon = accommodationCouponRepository.findByCustomerIdAndCouponGroupId(customerId, accommodationCouponGroupId).orElseThrow(() -> new
            AccommodationException(ErrorCode.NOT_MY_COUPON));

        if (accommodationCoupon.isUsedYN()) {
            throw new AccommodationException(ErrorCode.ALREADY_USED_COUPON);
        }

        AccommodationOrderItem accommodationOrderItem = accommodationOrderItemRepository.findById(accommodationOrderItemId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ORDER_ITEM));

        if (accommodationOrderItemRepository.findByCouponId(accommodationCoupon.getId()).isPresent()){
            throw new AccommodationException(ErrorCode.ALREADY_USED_COUPON);
        }

        accommodationOrderItem.setCouponId(accommodationCoupon.getId());
        accommodationOrderItem.setSalePrice(accommodationCouponGroup.getSalePrice());
        accommodationOrderItem.setPrice(accommodationOrderItem.getPrice()-accommodationOrderItem.getSalePrice());

        accommodationOrderItemRepository.save(accommodationOrderItem);
    }

    public void deleteCoupon(Long acccommodationOrderItemId) {
        AccommodationOrderItem accommodationOrderItem = accommodationOrderItemRepository.findById(acccommodationOrderItemId)
            .orElseThrow(() -> new AccommodationException(ErrorCode.NOT_FOUND_ORDER_ITEM));

        accommodationOrderItem.setCouponId(null);
        accommodationOrderItem.setPrice(accommodationOrderItem.getPrice()+accommodationOrderItem.getSalePrice());
        accommodationOrderItem.setSalePrice(0);

        accommodationOrderItemRepository.save(accommodationOrderItem);
    }


    private Integer totalPrice(List<AccommodationOrderItem> accommodationOrderItemList) {
        Integer total=0;
        for (AccommodationOrderItem i : accommodationOrderItemList) {
            total += i.getPrice();
        }
        return total;
    }
}
