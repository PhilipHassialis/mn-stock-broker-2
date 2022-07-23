package com.hassialis.philip.broker.wallet.error;

import com.hassialis.philip.broker.api.RestApiResponse;

public record CustomError(
                int status,
                String error,
                String message) implements RestApiResponse {
}
