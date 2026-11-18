package com.atlas.bank.atlas_bank.transaction.service.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public record TransactionExecutedEvent(
        Long transactionId,
        String type,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        BigDecimal fee
) {

    private static final DecimalFormat MONEY_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        MONEY_FORMAT = new DecimalFormat("#,##0.00", symbols);
        MONEY_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
    }

    public String formattedAmount() {
        BigDecimal value = amount == null ? BigDecimal.ZERO : amount;
        return MONEY_FORMAT.format(value);
    }
}