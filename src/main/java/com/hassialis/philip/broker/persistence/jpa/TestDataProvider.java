package com.hassialis.philip.broker.persistence.jpa;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
  private final SymbolsRepository symbols;

  public TestDataProvider(SymbolsRepository symbols) {
    this.symbols = symbols;
  }

  @EventListener
  public void init(StartupEvent event) {
    if (symbols.findAll().isEmpty()) {
      LOG.info("Initializing test data");
      Stream.of("AAPL", "AMZN", "META", "TSLA")
          .map(SymbolEntity::new)
          .forEach(symbols::save);
    }
  }

}
