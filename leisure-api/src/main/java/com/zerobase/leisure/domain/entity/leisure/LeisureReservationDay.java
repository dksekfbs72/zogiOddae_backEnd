package com.zerobase.leisure.domain.entity.leisure;

import com.zerobase.leisure.domain.entity.common.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Builder
@Setter
@Getter
public class LeisureReservationDay extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long leisureId;

	private String year;
	private LocalDateTime startAt;
	private LocalDateTime endAt;
}
