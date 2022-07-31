package com.hassialis.philip.broker.persistence.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "quote")
@Table(name = "quotes", schema = "mn")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne(targetEntity = SymbolEntity.class)
  @JoinColumn(name = "symbol", referencedColumnName = "value")
  private SymbolEntity symbol;
  private BigDecimal bid;
  private BigDecimal ask;
  @Column(name = "last_price")
  private BigDecimal lastPrice;
  private BigDecimal volume;

}
