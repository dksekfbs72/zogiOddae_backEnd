package com.zerobase.leisure.domain.repository.leisure;

import com.zerobase.leisure.domain.entity.leisure.LeisureReservationDay;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeisureReservationDayRepository extends JpaRepository<LeisureReservationDay, Long> {
	Optional<LeisureReservationDay> findByStartAtBetween(LocalDateTime startAt, LocalDateTime endAt);
	Optional<LeisureReservationDay> findByEndAtBetween(LocalDateTime startAt, LocalDateTime endAt);
}
