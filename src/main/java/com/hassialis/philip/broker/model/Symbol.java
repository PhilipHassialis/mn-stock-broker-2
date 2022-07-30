package com.hassialis.philip.broker.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Symbol", description = "Symbol")
public record Symbol(@Schema(description = "Symbol value", minLength = 1, maxLength = 5) String value) {
}
