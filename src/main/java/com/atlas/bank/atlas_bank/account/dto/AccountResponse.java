package com.atlas.bank.atlas_bank.account.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {

    private Long id;

	private String accountNumber;

	private String ownerName;

	private String email;

	private String type;

	private BigDecimal balance;

	private String status;

	private LocalDateTime createAt;
}
