package com.hassialis.philip.broker.wallet;

import com.hassialis.philip.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawFiatMoney(
                UUID accountId,
                UUID walletId,
                Symbol symbol,
                BigDecimal amount) {
}
