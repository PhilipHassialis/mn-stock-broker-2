package com.hassialis.philip.broker.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import com.github.javafaker.*;
import com.hassialis.philip.broker.model.Symbol;

@Singleton
public class InMemoryStore {

  private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
  private final Map<String, Symbol> symbols = new HashMap<>();
  private final Faker faker = new Faker();

  @PostConstruct
  public void initialize() {
    initializeWith(10);
  }

  public void initializeWith(int numberOfEntries) {
    symbols.clear();
    IntStream.range(0, numberOfEntries).forEach(i -> addNewSymbol());
  }

  private void addNewSymbol() {
    var symbol = new Symbol(faker.stock().nsdqSymbol());
    symbols.put(symbol.value(), symbol);
    LOG.debug("Added new symbol: {}", symbol);
  }

  public Map<String, Symbol> getSymbols() {
    return symbols;
  }

  public List<Symbol> getAllSymbols() {
    return symbols.values().stream().collect(Collectors.toList());
  }
}
