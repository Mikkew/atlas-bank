package com.atlas.bank.atlas_bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accountNumber;

	private String ownerName;

	private String email;

	private String type; //SAVING, CHECKING

	private BigDecimal balance;

	private String status; //ACTIVE, CLOSED, FROZEN

	private LocalDateTime createAt;
	
	@PrePersist
	public void prePersist() {
		this.createAt = LocalDateTime.now();
		if (status == null) status="ACTIVE";
		if (balance == null) balance = BigDecimal.ZERO;
	}
}
