package com.hassialis.philip.broker.model;

import java.math.BigDecimal;

public record Quote(Symbol symbol, BigDecimal bid, BigDecimal ask, BigDecimal lastPrice, BigDecimal volume) {

}
