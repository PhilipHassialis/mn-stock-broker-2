package com.hassialis.philip.broker.wallet;

import java.math.BigDecimal;
import java.util.UUID;

import com.hassialis.philip.broker.model.Symbol;

public record DepositFiatMoney(
                UUID accountId,
                UUID walletId,
                Symbol symbol,
                BigDecimal amount) {
}
