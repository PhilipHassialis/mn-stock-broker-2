package com.hassialis.philip.broker.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import com.github.javafaker.*;
import com.hassialis.philip.broker.model.Quote;
import com.hassialis.philip.broker.model.Symbol;

import io.netty.util.internal.ThreadLocalRandom;

@Singleton
public class InMemoryStore {

  private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
  private final Map<String, Symbol> symbols = new HashMap<>();
  private final Faker faker = new Faker();
  private final Map<String, Quote> cachedQuotes = new HashMap<>();
  private final ThreadLocalRandom current = ThreadLocalRandom.current();

  @PostConstruct
  public void initialize() {
    initializeWith(10);
  }

  public void initializeWith(int numberOfEntries) {
    symbols.clear();
    IntStream.range(0, numberOfEntries).forEach(i -> addNewSymbol());
    symbols.values().forEach(symbol -> cachedQuotes.put(symbol.value(), randomQuote(symbol)));
  }

  private Quote randomQuote(Symbol symbol) {
    return new Quote(symbol, randomValue(), randomValue(), randomValue(), randomValue());
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(current.nextDouble(1, 100));
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

  public Optional<Quote> fetchQuote(final String symbol) {
    return Optional.ofNullable(cachedQuotes.get(symbol));
  }

  public void update(Quote quote) {
    cachedQuotes.put(quote.symbol().value(), quote);
  }
}
