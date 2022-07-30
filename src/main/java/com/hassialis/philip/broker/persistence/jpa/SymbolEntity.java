package com.hassialis.philip.broker.persistence.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Symbol")
@Table(name = "symbols", schema = "mn")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolEntity {

  @Id
  private String value;

}