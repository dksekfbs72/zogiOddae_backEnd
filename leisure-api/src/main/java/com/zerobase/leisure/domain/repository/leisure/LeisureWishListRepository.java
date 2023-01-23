package com.zerobase.leisure.domain.repository.leisure;

import com.zerobase.leisure.domain.entity.leisure.LeisureWishList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeisureWishListRepository extends JpaRepository<LeisureWishList, Long> {
	Optional<LeisureWishList> findByMemberId(Long memberId);


	void deleteByMemberIdAndLeisureId(Long memberId, Long leisureId);

	Optional<Page<LeisureWishList>> findAllByMemberId(Long memberId, Pageable limit);

	Optional<LeisureWishList> findByMemberIdAndLeisureId(Long memberId, Long leisureId);
}
