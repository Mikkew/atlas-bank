package com.atlas.bank.atlas_bank.transaction.service.fee;

import java.math.BigDecimal;

public class CheckingFeeCalculator implements FeeCalculator {

    @Override
    public boolean supports(String accountType) {
        return "CHECKING".equals(accountType);
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.015"));
    }
}
