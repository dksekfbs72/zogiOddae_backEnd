package com.zerobase.leisure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.zerobase.leisure.domain.dto.leisure.LeisureWishListDto;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureWishList;
import com.zerobase.leisure.domain.form.LeisureForm;
import com.zerobase.leisure.domain.repository.leisure.LeisureWishListRepository;
import com.zerobase.leisure.service.leisure.LeisureWishService;
import com.zerobase.leisure.service.leisure.SellerLeisureService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class LeisureWishServiceTest {
	@Autowired
	private LeisureWishService leisureWishService;
	@Autowired
	private SellerLeisureService sellerLeisureService;
	@Autowired
	private LeisureWishListRepository leisureWishListRepository;

	@Test
	@DisplayName("찜목록 등록")
	void addLeisureWishTest() {
	    //given
		Leisure leisure = addLeisure();
		LeisureWishList leisureWishList = leisureWishService.addLeisureWish(1L, leisure.getId());
	    //when
	    //then
		assertEquals(leisureWishList.getMemberId(),1L);
		assertEquals(leisureWishList.getLeisureId(),leisure.getId());
	}

	@Test
	@DisplayName("찜목록 제거")
	void deleteLeisureWishTest() {
	    //given
		Leisure leisure = addLeisure();
		LeisureWishList leisureWishList = leisureWishService.addLeisureWish(1L, leisure.getId());
		leisureWishService.deleteLeisureWish(leisureWishList.getMemberId(),
			leisureWishList.getLeisureId());
		//when
		Optional<LeisureWishList> leisureWishList1 = leisureWishListRepository.findById(1L);
	    //then
		assertFalse(leisureWishList1.isPresent());
	}

	@Test
	@DisplayName("찜목록 불러오기")
	void getLeisureWishListTest() {
	    //given
		Leisure leisure = sellerLeisureService.AddLeisure(1L,LeisureForm.builder()
																	.addr("허리도 가늘군 만지면 부러지리")
																	.leisureName("산 레저")
																	.price(18000)
																	.pictureUrl("A://test/test.jpg")
																	.description("재밌는 곳입니다.")
																	.minPerson(2)
																	.maxPerson(8)
																	.lat(33.24342)
																	.lon(127.11132)
																	.build());
		Leisure leisure1 = sellerLeisureService.AddLeisure(1L,LeisureForm.builder()
																	.addr("허리도 가늘군 만지면")
																	.leisureName("바다 레저")
																	.price(20000)
																	.pictureUrl("B://test/test.jpg")
																	.description("재미없는 곳입니다.")
																	.minPerson(2)
																	.maxPerson(8)
																	.lat(33.24342)
																	.lon(127.11132)
																	.build());
		Leisure leisure2 = sellerLeisureService.AddLeisure(1L,LeisureForm.builder()
																	.addr("허리도 가늘군")
																	.leisureName("산바다 레저")
																	.price(30000)
																	.pictureUrl("C://test/test.jpg")
																	.description("재미 있는? 곳입니다.")
																	.minPerson(2)
																	.maxPerson(8)
																	.lat(33.24342)
																	.lon(127.11132)
																	.build());
		LeisureWishList leisureWishList = leisureWishService.addLeisureWish(1L, leisure.getId());
		LeisureWishList leisureWishList1 = leisureWishService.addLeisureWish(1L, leisure1.getId());
		LeisureWishList leisureWishList2 = leisureWishService.addLeisureWish(1L, leisure2.getId());
		//when
		List<LeisureWishListDto> list = null;
	    //then
		assertEquals(list.get(0).getName(), "산 레저");
		assertEquals(list.get(1).getName(), "바다 레저");
		assertEquals(list.get(2).getName(), "산바다 레저");
		assertEquals(list.get(0).getAddr(), "허리도 가늘군 만지면 부러지리");
		assertEquals(list.get(1).getAddr(), "허리도 가늘군 만지면");
		assertEquals(list.get(2).getAddr(), "허리도 가늘군");
		assertEquals(list.get(0).getPrice(), 18000);
		assertEquals(list.get(1).getPrice(), 20000);
		assertEquals(list.get(2).getPrice(), 30000);
		assertEquals(list.get(0).getPictureUrl(), "A://test/test.jpg");
		assertEquals(list.get(1).getPictureUrl(), "B://test/test.jpg");
		assertEquals(list.get(2).getPictureUrl(), "C://test/test.jpg");
	}

	private Leisure addLeisure() {
		LeisureForm form = LeisureForm.builder()
			.addr("허리도 가늘군 만지면 부러지리")
			.leisureName("바다 레저")
			.price(18000)
			.pictureUrl("D://test/test.jpg")
			.description("재밌는 곳입니다.")
			.minPerson(2)
			.maxPerson(8)
			.lat(33.24342)
			.lon(127.11132)
			.build();

		return sellerLeisureService.AddLeisure(1L,form);
	}

}
