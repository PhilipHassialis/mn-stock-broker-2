package com.hassialis.philip.broker.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Column;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class QuoteDTO {

  private Integer id;
  private BigDecimal volume;
  @Column(name = "last_price")
  private BigDecimal lastPrice;

  public BigDecimal getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(BigDecimal lastPrice) {
    this.lastPrice = lastPrice;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

}
