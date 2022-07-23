package com.hassialis.philip.broker.error;

public record CustomError(int status, String error, String message, String path) {
}
