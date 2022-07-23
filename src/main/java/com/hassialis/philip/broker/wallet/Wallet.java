package com.hassialis.philip.broker.wallet;

import com.hassialis.philip.broker.Symbol;
import com.hassialis.philip.broker.api.RestApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked) implements RestApiResponse {
    public Wallet addAvailable(BigDecimal amount) {
        return new Wallet(this.accountId, this.walletId, this.symbol, this.available.add(amount), this.locked);
    }
}
