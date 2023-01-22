package com.zerobase.leisure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.leisure.domain.dto.leisure.LeisureDto;
import com.zerobase.leisure.domain.entity.leisure.Leisure;
import com.zerobase.leisure.domain.entity.leisure.LeisureDayOff;
import com.zerobase.leisure.domain.form.LeisureDayOffForm;
import com.zerobase.leisure.domain.form.LeisureForm;
import com.zerobase.leisure.domain.repository.leisure.LeisureDayOffRepository;
import com.zerobase.leisure.domain.repository.leisure.LeisureRepository;
import com.zerobase.leisure.service.leisure.SellerLeisureService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@RequiredArgsConstructor
class SellerLeisureServiceTest {

	@Autowired
	private SellerLeisureService sellerLeisureService;
	@Autowired
	private LeisureRepository leisureRepository;
	@Autowired
	private LeisureDayOffRepository leisureDayOffRepository;

	@Test
	@Transactional
	@DisplayName("시설 생성")
	void AddLeisureTest() {
		Leisure leisure = addLeisure();

		assertEquals(leisure.getSellerId(), 1L);
		assertEquals(leisure.getLeisureName(), "바다 레저");
		assertEquals(leisure.getPrice(), 18000);
		assertEquals(leisure.getPictureUrl(), "D://test/test.jpg");
		assertEquals(leisure.getAddr(), "허리도 가늘군 만지면 부러지리");
		assertEquals(leisure.getDescription(), "재밌는 곳입니다.");
		assertEquals(leisure.getMinPerson(), 2);
		assertEquals(leisure.getMaxPerson(), 8);
		assertEquals(leisure.getLat(), 33.24342);
		assertEquals(leisure.getLon(), 127.11132);
	}

	@Test
	@Transactional
	@DisplayName("보유 시설 불러오기")
	void getAllLeisureTest() {
		//given
		for (int i=0; i<3; i++){
			sellerLeisureService.AddLeisure(1L, LeisureForm.builder()
				.addr("허리도 가늘군 만지면 부러지리")
				.leisureName("바다 레저")
				.price(18000)
				.pictureUrl("D://test/test.jpg")
				.description("재밌는 곳입니다.")
				.minPerson(i+1)
				.maxPerson(8)
				.lat(33.24342)
				.lon(127.11132)
				.build());
		}
		//when
		List<LeisureDto> leisureList = sellerLeisureService.getAllLeisure(1L, Pageable.ofSize(0)).toList();
		//then
		assertEquals(leisureList.get(0).getSellerId(),1L);
		assertEquals(leisureList.get(0).getName(), "바다 레저");
		assertEquals(leisureList.get(0).getPrice(), 18000);
		assertEquals(leisureList.get(0).getPictureUrl(), "D://test/test.jpg");
		assertEquals(leisureList.get(0).getAddr(), "허리도 가늘군 만지면 부러지리");
		assertEquals(leisureList.get(0).getDescription(), "재밌는 곳입니다.");
		assertEquals(leisureList.get(0).getMaxPerson(), 8);
		assertEquals(leisureList.get(0).getLat(), 33.24342);
		assertEquals(leisureList.get(0).getLon(), 127.11132);
		assertEquals(leisureList.get(0).getMinPerson(), 1);
		assertEquals(leisureList.get(1).getMinPerson(), 2);
		assertEquals(leisureList.get(2).getMinPerson(), 3);
	}

	@Test
	@Transactional
	@DisplayName("시설 정보 수정")
	void updateLeisureTest() {
		Leisure leisure = addLeisure();

		assertEquals(leisure.getSellerId(), 1L);
		assertEquals(leisure.getLeisureName(), "바다 레저");
		assertEquals(leisure.getPrice(), 18000);
		assertEquals(leisure.getPictureUrl(), "D://test/test.jpg");
		assertEquals(leisure.getAddr(), "허리도 가늘군 만지면 부러지리");
		assertEquals(leisure.getDescription(), "재밌는 곳입니다.");
		assertEquals(leisure.getMinPerson(), 2);
		assertEquals(leisure.getMaxPerson(), 8);
		assertEquals(leisure.getLat(), 33.24342);
		assertEquals(leisure.getLon(), 127.11132);

		LeisureForm form1 = LeisureForm.builder()
			.addr("허리도 가늘군")
			.leisureName("산 레저")
			.price(20000)
			.pictureUrl("C://test2/test2.jpg")
			.description("재미없는 곳입니다.")
			.minPerson(5)
			.maxPerson(10)
			.lat(333.24342)
			.lon(1270.11132)
			.build();

		Leisure leisure1 = sellerLeisureService.updateLeisure(leisure.getId(), form1);

		assertEquals(leisure1.getSellerId(), 1L);
		assertEquals(leisure1.getLeisureName(), "산 레저");
		assertEquals(leisure1.getPrice(), 20000);
		assertEquals(leisure1.getPictureUrl(), "C://test2/test2.jpg");
		assertEquals(leisure1.getAddr(), "허리도 가늘군");
		assertEquals(leisure1.getDescription(), "재미없는 곳입니다.");
		assertEquals(leisure1.getMinPerson(), 5);
		assertEquals(leisure1.getMaxPerson(), 10);
		assertEquals(leisure1.getLat(), 333.24342);
		assertEquals(leisure1.getLon(), 1270.11132);
	}

	@Test
	@Transactional
	@DisplayName("시설 삭제")
	void deleteLeisureTest() {
		Leisure leisure = addLeisure();
		sellerLeisureService.deleteLeisure(leisure.getId(), 1L);

		Optional<Leisure> leisure1 = leisureRepository.findById(leisure.getId());

		assertFalse(leisure1.isPresent());
	}

	@Test
	@Transactional
	@DisplayName("휴일 지정")
	void addLeisureDayOffTest() {
		//given
		addLeisure();
		//when
		LeisureDayOff leisureDayOff = addLeisureDayOff();
		//then
		assertNotNull(leisureDayOff);
		assertEquals(leisureDayOff.getYear(),"2023");
		assertEquals(leisureDayOff.getStartAt(),LocalDate.of(2023,1,1));
		assertEquals(leisureDayOff.getEndAt(),LocalDate.of(2023,1,2));
	}

	@Test
	@Transactional
	@DisplayName("휴일 삭제")
	void deleteLeisureDayOffTest() {
		//given
		addLeisure();
		LeisureDayOff leisureDayOff = addLeisureDayOff();
		sellerLeisureService.deleteLeisureDayOff(leisureDayOff.getId());
		//when
		Optional<LeisureDayOff> leisureDayOff1 = leisureDayOffRepository.findById(leisureDayOff.getId());
		//then
		assertFalse(leisureDayOff1.isPresent());
	}

	@Test
	@Transactional
	@DisplayName("휴일 불러오기")
	void getLeisureDayOffTest() {
		//given
		addLeisure();
		sellerLeisureService.addLeisureDayOff(1L, LeisureDayOffForm.builder()
			.startDay(LocalDate.of(2023,1,1))
			.endDay(LocalDate.of(2023,1,2))
			.build());
		sellerLeisureService.addLeisureDayOff(1L, LeisureDayOffForm.builder()
			.startDay(LocalDate.of(2023,1,2))
			.endDay(LocalDate.of(2023,1,3))
			.build());
		sellerLeisureService.addLeisureDayOff(1L, LeisureDayOffForm.builder()
			.startDay(LocalDate.of(2023,1,3))
			.endDay(LocalDate.of(2023,1,4))
			.build());
		//when
		List<LeisureDayOff> leisureDayOffList = null;
		//then
		assertEquals(leisureDayOffList.get(0).getYear(), "2023");
		assertEquals(leisureDayOffList.get(1).getYear(), "2023");
		assertEquals(leisureDayOffList.get(2).getYear(), "2023");
		assertEquals(leisureDayOffList.get(0).getStartAt(), LocalDate.of(2023,1,1));
		assertEquals(leisureDayOffList.get(1).getStartAt(), LocalDate.of(2023,1,2));
		assertEquals(leisureDayOffList.get(2).getStartAt(), LocalDate.of(2023,1,3));
		assertEquals(leisureDayOffList.get(0).getEndAt(), LocalDate.of(2023,1,2));
		assertEquals(leisureDayOffList.get(1).getEndAt(), LocalDate.of(2023,1,3));
		assertEquals(leisureDayOffList.get(2).getEndAt(), LocalDate.of(2023,1,4));
	}

	@Test
	@Transactional
	@DisplayName("휴일 수정하기")
	void updateLeisureDayOffTest() {
		//given
		addLeisure();
		LeisureDayOff leisureDayOff = addLeisureDayOff();
		sellerLeisureService.updateLeisureDayOff(leisureDayOff.getId(),
			LeisureDayOffForm.builder()
				.startDay(LocalDate.of(2024,2,4))
				.endDay(LocalDate.of(2024,2,5))
				.build());
		//when
		LeisureDayOff leisureDayOff1 = leisureDayOffRepository.findById(leisureDayOff.getId()).get();
		//then
		assertEquals(leisureDayOff1.getYear(), "2024");
		assertEquals(leisureDayOff1.getStartAt(), LocalDate.of(2024,2,4));
		assertEquals(leisureDayOff1.getEndAt(), LocalDate.of(2024,2,5));
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

	private LeisureDayOff addLeisureDayOff() {
		return sellerLeisureService.addLeisureDayOff(1L, LeisureDayOffForm.builder()
			.startDay(LocalDate.of(2023,1,1))
			.endDay(LocalDate.of(2023,1,2))
			.build());
	}
}
